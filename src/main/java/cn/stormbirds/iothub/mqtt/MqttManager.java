package cn.stormbirds.iothub.mqtt;

import cn.stormbirds.iothub.base.ResultCode;
import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.entity.MqttConfig;
import cn.stormbirds.iothub.exception.BizException;
import cn.stormbirds.iothub.service.IMqttConfigService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ Description cn.stormbirds.iothub.mqtt
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/12 11:02
 */
@Component
@Slf4j
public class MqttManager {
    private final Map<Long,MqttAcceptClient> mqttAcceptClientMap = new ConcurrentHashMap<>();

    @Lazy
    @Resource
    private IMqttConfigService mqttConfigService;

    public boolean startMqtt(long id){
        MqttConfig mqttConfig = mqttConfigService.getById(id);
        if(mqttConfig==null) return false;
        MqttProperties mqttProperties = new MqttProperties(mqttConfig);
        if(mqttAcceptClientMap.get(id)==null){
            mqttAcceptClientMap.put(id,new MqttAcceptClient());
        }else if(mqttAcceptClientMap.get(id).getClient().isConnected()){
            return true;
        }
        return mqttAcceptClientMap.get(id).connect(mqttProperties, new MqttAcceptCallback(mqttAcceptClientMap.get(id),mqttConfigService,id));
    }

    public boolean stopMqtt(long id){
        if(mqttAcceptClientMap.get(id)==null){
            return false;
        }
        return mqttAcceptClientMap.get(id).closeConnection();
    }

    public boolean sendMessage(Long id, String topic, Integer qos, String message) {
        if(mqttAcceptClientMap.get(id)==null || !mqttAcceptClientMap.get(id).getClient().isConnected()){
            return false;
        }
        try {
            mqttAcceptClientMap.get(id).getClient().publish(topic,message.getBytes(StandardCharsets.UTF_8),qos,false);
            return true;
        } catch (MqttException e) {
            throw new BizException(ResultJson.failure(ResultCode.MQTT_SENDMESSAGE_ERROR,e.getMessage()));
        }
    }
}
