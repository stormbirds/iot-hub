package cn.stormbirds.iothub.controller;

import cn.stormbirds.iothub.base.ResultCode;
import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.entity.MqttConfig;
import cn.stormbirds.iothub.mqtt.MqttProperties;
import cn.stormbirds.iothub.service.IMqttConfigService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author stormbirds
 * @since 2022-09-11
 */
@RestController
@RequestMapping("/api/v1/mqttConfig")
public class MqttConfigController {

    @Resource
    private IMqttConfigService mqttConfigService;

    @PostMapping("/save")
    public ResultJson save(@Validated @RequestBody MqttProperties mqttProperties){
        return mqttConfigService.save(mqttProperties)?ResultJson.ok():ResultJson.failure(ResultCode.SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResultJson update(@RequestBody MqttConfig mqttConfig){
        return mqttConfigService.updateById(mqttConfig)?ResultJson.ok():ResultJson.failure(ResultCode.SERVER_ERROR);
    }

    @PostMapping("/testMqtt")
    public ResultJson testMqtt(@Validated @RequestBody MqttProperties mqttProperties){
        return mqttConfigService.testMqttChannel(mqttProperties)?ResultJson.ok("连接成功"):ResultJson.failure(ResultCode.SERVER_ERROR);
    }

    @GetMapping("/start/{id}")
    public ResultJson enableMqtt(@PathVariable Integer id) throws MqttException {
        return mqttConfigService.start(id);
    }

    @GetMapping("/stop/{id}")
    public ResultJson disableMqtt(@PathVariable Integer id) throws MqttException {
        return mqttConfigService.stop(id);
    }
}
