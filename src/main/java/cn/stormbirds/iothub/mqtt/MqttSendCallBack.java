package cn.stormbirds.iothub.mqtt;

import cn.stormbirds.iothub.service.IMqttConfigService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
public class MqttSendCallBack implements MqttCallbackExtended {

    private Long mqttId;
    private MqttAcceptClient mqttAcceptClient;
    private IMqttConfigService mqttConfigService;

    public MqttSendCallBack(Long mqttId, MqttAcceptClient mqttAcceptClient, IMqttConfigService mqttConfigService) {
        this.mqttId = mqttId;
        this.mqttAcceptClient = mqttAcceptClient;
        this.mqttConfigService = mqttConfigService;
    }

    /**
     * 客户端断开后触发
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("发送消息回调: 连接断开，可以做重连");
    }

    /**
     * 客户端收到消息触发
     *
     * @param topic       主题
     * @param mqttMessage 消息
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        log.info("发送消息回调:  接收消息主题 : " + topic);
        log.info("发送消息回调:  接收消息内容 : " + new String(mqttMessage.getPayload()));
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
            log.info("发送消息回调:  向主题：" + topic + "发送消息成功！");
        }
        try {
            MqttMessage message = token.getMessage();
            byte[] payload = message.getPayload();
            String s = new String(payload, StandardCharsets.UTF_8);
            log.debug("发送消息回调:  消息的内容是：" + s);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接emq服务器后触发
     *
     * @param b
     * @param s
     */
    @Override
    public void connectComplete(boolean b, String s) {
        log.info("--------------------ClientId:"
                + this.mqttAcceptClient.getClient().getClientId() + "客户端连接成功！--------------------");
    }
}