package cn.stormbirds.iothub.mqtt;

import cn.stormbirds.iothub.entity.MqttConfig;
import cn.stormbirds.iothub.service.IMqttConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
public class MqttContainer {
    private final Map<Long,MqttAcceptClient> mqttAcceptClientMap = new ConcurrentHashMap<>();

    @Resource
    private IMqttConfigService mqttConfigService;

    public boolean startMqtt(long id){
        MqttConfig mqttConfig = mqttConfigService.getById(id);
        if(mqttConfig==null) return false;
        if(MqttConstant.ONLINE.equalsIgnoreCase(mqttConfig.getStatus())) return true;
        MqttProperties mqttProperties = new MqttProperties(mqttConfig);
        return mqttAcceptClientMap.get(id).connect(mqttProperties);
    }
}
