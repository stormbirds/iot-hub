package cn.stormbirds.iothub.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.util.ObjectUtils;

@Slf4j
public class MqttAcceptClient {

    private MqttProperties mqttProperties;

    private MqttAcceptCallback mqttAcceptCallback;

    private MqttClient client;

    public MqttClient getClient() {
        return client;
    }

    private void setProperties(MqttProperties mqttProperties){
        this.mqttProperties = mqttProperties;
    }
    private void setClient(MqttClient client) {
        this.client = client;
    }

    private void setAcceptCallback(MqttAcceptCallback mqttAcceptCallback){
        this.mqttAcceptCallback = mqttAcceptCallback;
    }


    /**
     * 客户端连接
     * @return 返回连接是否成功
     */
    public boolean connect(MqttProperties mqttProperties, MqttAcceptCallback mqttAcceptCallback/*,MqttSendCallBack mqttSendCallBack*/) {
        this.mqttProperties = null;
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
                options.setWill(mqttProperties.getWillTopic(),
                        mqttProperties.getWillMessage().getBytes(),
                        mqttProperties.getWillQos(),
                        mqttProperties.getWillRetain());
            try {
                // 设置回调
                client.setCallback(mqttAcceptCallback);
                client.connect(options);
                setClient(client);
                setAcceptCallback(mqttAcceptCallback);
                setProperties(mqttProperties);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean closeConnection(){
        if(this.client!=null && this.client.isConnected()){
            try {
                this.client.close();
                return true;
            } catch (MqttException e) {
                log.error("关闭mqtt出错",e);
            }
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
            log.error("mqtt重连出错",e);
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
            log.error("mqtt订阅主题出错",e);
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