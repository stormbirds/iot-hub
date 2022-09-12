package cn.stormbirds.iothub.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

@Component
@Slf4j
public class MqttAcceptClient {

    @Lazy
    @Resource
    private MqttAcceptCallback mqttAcceptCallback;

    public static MqttClient client;

    private static MqttClient getClient() {
        return client;
    }

    private static void setClient(MqttClient client) {
        MqttAcceptClient.client = client;
    }

    /**
     * 客户端连接
     * @return 返回连接是否成功
     */
    public boolean connect(MqttProperties mqttProperties) {
        MqttClient client;
        try {
            String hostUrl = String.format(MqttConstant.URL_FORMAT,mqttProperties.getProtocol(),mqttProperties.getHost(),mqttProperties.getPort());
            client = new MqttClient(hostUrl, mqttProperties.getClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(mqttProperties.getUsername());
            options.setPassword(mqttProperties.getPassword().toCharArray());
            options.setConnectionTimeout(mqttProperties.getTimeout());
            options.setKeepAliveInterval(mqttProperties.getKeepAlive());
            options.setAutomaticReconnect(mqttProperties.getReconnect());
            options.setCleanSession(mqttProperties.getCleanSession());
            switch (mqttProperties.getVersion()){
                case "3.1":
                    options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
                    break;
                case "3.1.1":
                    options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
                    break;
                default:
                    options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_DEFAULT);
            }
            if(!ObjectUtils.isEmpty(mqttProperties.getWillTopic()) )
                options.setWill(mqttProperties.getWillTopic(),mqttProperties.getWillMessage().getBytes(),mqttProperties.getWillQos(),mqttProperties.getWillRetain());
            try {
                // 设置回调
                client.setCallback(mqttAcceptCallback);
                client.connect(options);
                MqttAcceptClient.setClient(client);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 重新连接
     */
    public void reconnection() {
        try {
            client.reconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   连接方式
     */
    public void subscribe(String topic, int qos) {
        log.info("==============开始订阅主题==============" + topic);
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消订阅某个主题
     *
     * @param topic 主题
     */
    public void unsubscribe(String topic) {
        log.info("==============开始取消订阅主题==============" + topic);
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}