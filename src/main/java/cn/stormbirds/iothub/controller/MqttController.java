package cn.stormbirds.iothub.controller;

import cn.hutool.core.util.IdUtil;
import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.mqtt.MqttAcceptClient;
import cn.stormbirds.iothub.mqtt.MqttProperties;
import cn.stormbirds.iothub.service.IMqttConfigService;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.*;

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
@Slf4j
@Tag(name = "MQTT")
@RestController
@RequestMapping("/api/v1/mqtt")
public class MqttController {

    @Resource
    private IMqttConfigService mqttConfigService;
    @PostMapping("/sql/{db_name}")
    public String sql(@PathVariable String db_name, @RequestBody String sql){
        log.info("接收到SQL语句 目的数据库 {} SQL语句：{}",db_name,sql);
        return "{\n" +
                "    \"code\": 0,\n" +
                "    \"column_meta\": [\n" +
                "        [\n" +
                "            \"name\",\n" +
                "            \"VARCHAR\",\n" +
                "            64\n" +
                "        ],\n" +
                "        [\n" +
                "            \"ntables\",\n" +
                "            \"BIGINT\",\n" +
                "            8\n" +
                "        ],\n" +
                "        [\n" +
                "            \"status\",\n" +
                "            \"VARCHAR\",\n" +
                "            10\n" +
                "        ]\n" +
                "    ],\n" +
                "    \"data\": [\n" +
                "        [\n" +
                "            \"information_schema\",\n" +
                "            16,\n" +
                "            \"ready\"\n" +
                "        ],\n" +
                "        [\n" +
                "            \"performance_schema\",\n" +
                "            9,\n" +
                "            \"ready\"\n" +
                "        ]\n" +
                "    ],\n" +
                "    \"rows\": 2\n" +
                "}";
    }

    @GetMapping("/sendMessage/{id}")
    public ResultJson sendMessage(@PathVariable Long id,@RequestParam String topic,@RequestParam Integer qos,@RequestParam String message){
        return mqttConfigService.sendMessage(id,topic,qos,message);
    }

//    @GetMapping("/send")
//    public ResultJson<String> sendMessage(@RequestParam String topic,@RequestParam String message) throws MqttException {
//        MqttAcceptClient.client.publish(topic,message.getBytes(),0,false);
//        List<Map<String,Object>> result = new ArrayList<>();
//        for (int i = 0; i < 2000; i++) {
//            Map<String,Object> sendMsg = new HashMap<>();
//            sendMsg.put("tagnum", IdUtil.nanoId(15));
//            sendMsg.put("v",System.currentTimeMillis());
//            result.add(sendMsg);
//        }
//        for (int i = 0; i < 20; i++) {
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        while (true){
//                            MqttAcceptClient.client.publish("iot", JSON.toJSONString(result).getBytes(),0,false);
//                            Thread.sleep(2000);
//                        }
//                    } catch (MqttException | InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            thread.start();
//        }
//        return ResultJson.ok("ok");
//    }
}
