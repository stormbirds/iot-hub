package cn.stormbirds.iothub.controller;

import cn.hutool.core.util.IdUtil;
import cn.stormbirds.iothub.mqtt.MqttAcceptClient;
import com.alibaba.fastjson.JSON;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Description cn.stormbirds.iothub.controller
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/8 1:36
 */
@RequestMapping
@RestController("/mqtt")
public class SendController {

    @Resource
    private MqttAcceptClient mqttAcceptClient;

    @GetMapping("/send")
    public String sendMessage(@RequestParam String topic,@RequestParam String message) throws MqttException {
        MqttAcceptClient.client.publish(topic,message.getBytes(),0,false);
        List<Map> result = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            Map<String,Object> sendMsg = new HashMap<>();
            sendMsg.put("tagnum", IdUtil.nanoId(15));
            sendMsg.put("v",System.currentTimeMillis());
            result.add(sendMsg);
        }
        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true){
                            MqttAcceptClient.client.publish("iot", JSON.toJSONString(result).getBytes(),0,false);
                            Thread.sleep(2000);
                        }
                    } catch (MqttException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        return "ok";
    }
}
