package cn.stormbirds.iothub.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.stormbirds.iothub.base.ResultCode;
import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.entity.MqttConfig;
import cn.stormbirds.iothub.mapper.MqttConfigMapper;
import cn.stormbirds.iothub.mqtt.MqttAcceptClient;
import cn.stormbirds.iothub.mqtt.MqttConstant;
import cn.stormbirds.iothub.mqtt.MqttProperties;
import cn.stormbirds.iothub.service.IMqttConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stormbirds
 * @since 2022-09-11
 */
@Service
public class MqttConfigServiceImpl extends ServiceImpl<MqttConfigMapper, MqttConfig> implements IMqttConfigService {

    @Resource
    private MqttAcceptClient mqttAcceptClient;

    @Override
    public boolean save(MqttProperties mqttProperties) {
        MqttConfig mqttConfig = new MqttConfig()
                .setName(mqttProperties.getName())
                .setClientId(mqttProperties.getClientId())
                .setHost(mqttProperties.getHost())
                .setPort(mqttProperties.getPort())
                .setProtocol(mqttProperties.getProtocol())
                .setUsername(mqttProperties.getUsername())
                .setPassword(mqttProperties.getPassword())
                .setTimeout(mqttProperties.getTimeout())
                .setKeepalive(mqttProperties.getKeepAlive())
                .setCleanSession(mqttProperties.getCleanSession())
                .setReconnect(mqttProperties.getReconnect())
                .setVersion(mqttProperties.getVersion())
                .setSessionTimeout(mqttProperties.getSessionTimeout())
                .setWillTopic(mqttProperties.getWillTopic())
                .setWillQos(mqttProperties.getWillQos())
                .setWillRetain(mqttProperties.getWillRetain())
                .setWillMessage(mqttProperties.getWillMessage())
                .setWillDelay(mqttProperties.getWillDelay())
                .setQos(mqttProperties.getQos())
                .setStatus(MqttConstant.OFFLINE)
                .setEnable(false)
                .setCreateAt(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_FORMATTER))
                .setUpdateAt(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_FORMATTER))
                ;

        return save(mqttConfig);
    }

    @Override
    public ResultJson start(Integer id) throws MqttException {
        MqttConfig mqttConfig = getById(id);
        if(MqttAcceptClient.client.isConnected()){
            MqttAcceptClient.client.close(true);
        }
        MqttProperties mqttProperties = new MqttProperties(mqttConfig);
        mqttConfig.setUpdateAt(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_FORMATTER));
        mqttConfig.setEnable(true);
        return mqttAcceptClient.connect(mqttProperties)?ResultJson.ok():ResultJson.failure(ResultCode.SERVER_ERROR);
    }

    @Override
    public void offline() {
        updateBatchById(list().stream()
                .peek(mqttConfig -> {
                    mqttConfig.setStatus(MqttConstant.OFFLINE);
                    mqttConfig.setLastDisconnectionTime(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_FORMATTER));
                })
                .collect(Collectors.toList()));
    }

    @Override
    public void online() {
        updateBatchById(list().stream()
                .peek(mqttConfig -> {
                    mqttConfig.setStatus(MqttConstant.ONLINE);
                    mqttConfig.setLastConnectionTime(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_FORMATTER));
                })
                .collect(Collectors.toList()));
    }

    @Override
    public ResultJson stop(Integer id) throws MqttException {
        if(MqttAcceptClient.client.isConnected()){
            MqttAcceptClient.client.close(true);
        }
        return updateBatchById(list().stream()
                .peek(mqttConfig -> {
                    mqttConfig.setUpdateAt(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_FORMATTER));
                    mqttConfig.setEnable(false);
                })
                .collect(Collectors.toList()))?ResultJson.ok():ResultJson.failure(ResultCode.SERVER_ERROR);
    }

    @Override
    public boolean testMqttChannel(MqttProperties mqttProperties){
        MqttClient client;
        try {
            String hostUrl = String.format(MqttConstant.URL_FORMAT,mqttProperties.getProtocol(),mqttProperties.getHost(),mqttProperties.getPort());
            client = new MqttClient(hostUrl, mqttProperties.getClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(mqttProperties.getUsername());
            options.setPassword(mqttProperties.getPassword().toCharArray());

            try {
                client.connect(options);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                client.close(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
