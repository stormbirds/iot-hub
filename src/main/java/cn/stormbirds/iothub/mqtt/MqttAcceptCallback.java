package cn.stormbirds.iothub.mqtt;

import cn.stormbirds.iothub.service.IMqttConfigService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class MqttAcceptCallback implements MqttCallbackExtended {

    @Resource
    private MqttAcceptClient mqttAcceptClient;
    @Resource
    private IMqttConfigService mqttConfigService;

    /**
     * 客户端断开后触发
     *
     * @param throwable 连接丢失原因
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("接收消息回调:  连接断开，开始重连");
        mqttConfigService.offline();
        if (MqttAcceptClient.client == null || !MqttAcceptClient.client.isConnected()) {
            log.info("接收消息回调:  emqx重新连接....................................................");
            mqttAcceptClient.reconnection();
        }
    }

    /**
     * 客户端收到消息触发
     *
     * @param topic       主题
     * @param mqttMessage 消息
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        log.info("接收消息回调:  接收消息主题 : " + topic);
        log.info("接收消息回调:  接收消息内容 : " + new String(mqttMessage.getPayload()));
    }

    /**
     * 发布消息成功
     *
     * @param token token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        String[] topics = token.getTopics();
        for (String topic : topics) {
            log.info("接收消息回调:  向主题：" + topic + "发送消息成功！");
        }
        try {
            MqttMessage message = token.getMessage();
            byte[] payload = message.getPayload();
            String s = new String(payload, StandardCharsets.UTF_8);
            log.info("接收消息回调:  消息的内容是：" + s);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接emq服务器后触发
     *
     * @param b 如果为true，连接是自动重新连接的结果。
     * @param s 连接的服务器地址
     */
    @Override
    public void connectComplete(boolean b, String s) {
        mqttConfigService.online();
        log.info("--------------------客户端ClientId:{} 与服务端 {} {} ！--------------------"
                , MqttAcceptClient.client.getClientId(), s ,b?"系统自动重新连接成功": "连接成功");
        // 以/#结尾表示订阅所有子集的主题
        // 订阅所有客户端上下线主题
        mqttAcceptClient.subscribe("$SYS/brokers/+/clients/#", 0);
    }
}