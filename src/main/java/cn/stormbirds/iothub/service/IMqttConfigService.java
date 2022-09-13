package cn.stormbirds.iothub.service;

import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.entity.MqttConfig;
import cn.stormbirds.iothub.mqtt.MqttProperties;
import com.baomidou.mybatisplus.extension.service.IService;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stormbirds
 * @since 2022-09-11
 */
public interface IMqttConfigService extends IService<MqttConfig> {

    boolean testMqttChannel(MqttProperties mqttProperties);
    boolean save(MqttProperties mqttProperties);

    boolean start(Long id);

    void offline(Long mqttId);

    void online(Long mqttId);

    boolean stop(Long id);

    boolean justStopService(Long id);

    ResultJson sendMessage(Long id, String topic, Integer qos, String message);
}
