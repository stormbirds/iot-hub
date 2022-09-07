package cn.stormbirds.iothub;

import cn.hutool.core.util.IdUtil;
import cn.stormbirds.iothub.mqtt.MqttAcceptClient;
import com.alibaba.fastjson.JSON;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class IotHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotHubApplication.class, args);
    }

    @PostConstruct
    public void test(){
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
                        MqttAcceptClient.client.publish("iot", JSON.toJSONString(result).getBytes(),0,false);
                        Thread.sleep(2000);
                    } catch (MqttException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }
}
