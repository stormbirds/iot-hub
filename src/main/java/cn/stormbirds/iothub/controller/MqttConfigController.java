package cn.stormbirds.iothub.controller;

import cn.stormbirds.iothub.base.ResultCode;
import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.entity.Channel;
import cn.stormbirds.iothub.entity.MqttConfig;
import cn.stormbirds.iothub.mqtt.MqttConstant;
import cn.stormbirds.iothub.mqtt.MqttProperties;
import cn.stormbirds.iothub.service.IMqttConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.util.ObjectUtils;
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

    @PostMapping("/delete/{id}")
    public ResultJson delete(@PathVariable Long id){
        if(MqttConstant.ONLINE.equalsIgnoreCase(mqttConfigService.getById(id).getStatus()) ){
            mqttConfigService.stop(id);
        }
        return ResultJson.ok( mqttConfigService.removeById(id));
    }

    @PostMapping("/testMqtt")
    public ResultJson testMqtt(@Validated @RequestBody MqttProperties mqttProperties){
        return mqttConfigService.testMqttChannel(mqttProperties)?ResultJson.ok("连接成功"):ResultJson.failure(ResultCode.SERVER_ERROR);
    }

    @GetMapping("/start/{id}")
    public ResultJson enableMqtt(@PathVariable Long id) {
        return ResultJson.ok(mqttConfigService.start(id));
    }

    @GetMapping("/stop/{id}")
    public ResultJson disableMqtt(@PathVariable Long id) {
        return ResultJson.ok(mqttConfigService.stop(id));
    }

    @GetMapping("/list")
    public ResultJson list(@RequestBody MqttConfig mqttConfig){
        QueryWrapper<MqttConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!ObjectUtils.isEmpty(mqttConfig.getId()),"id",mqttConfig.getId());
        queryWrapper.like(!ObjectUtils.isEmpty(mqttConfig.getName()),"name",mqttConfig.getName());
        queryWrapper.like(!ObjectUtils.isEmpty(mqttConfig.getHost()),"host",mqttConfig.getHost());
        queryWrapper.eq(!ObjectUtils.isEmpty(mqttConfig.getPort()),"port",mqttConfig.getPort());
        queryWrapper.eq(!ObjectUtils.isEmpty(mqttConfig.getProtocol()),"protocol",mqttConfig.getProtocol());
        queryWrapper.like(!ObjectUtils.isEmpty(mqttConfig.getUsername()),"username",mqttConfig.getUsername());
        queryWrapper.like(!ObjectUtils.isEmpty(mqttConfig.getClientId()),"client_id",mqttConfig.getClientId());
        queryWrapper.eq(!ObjectUtils.isEmpty(mqttConfig.getCleanSession()),"clean_session",mqttConfig.getCleanSession());
        queryWrapper.eq(!ObjectUtils.isEmpty(mqttConfig.getReconnect()),"reconnect",mqttConfig.getReconnect());
        queryWrapper.eq(!ObjectUtils.isEmpty(mqttConfig.getVersion()),"version",mqttConfig.getVersion());
        queryWrapper.eq(!ObjectUtils.isEmpty(mqttConfig.getStatus()),"status",mqttConfig.getStatus());
        queryWrapper.eq(!ObjectUtils.isEmpty(mqttConfig.getEnable()),"enable",mqttConfig.getEnable());
        queryWrapper.like(!ObjectUtils.isEmpty(mqttConfig.getWillTopic()),"will_topic",mqttConfig.getWillTopic());
        queryWrapper.eq(!ObjectUtils.isEmpty(mqttConfig.getWillQos()),"will_qos",mqttConfig.getWillQos());
        queryWrapper.eq(!ObjectUtils.isEmpty(mqttConfig.getWillRetain()),"will_retain",mqttConfig.getWillRetain());
        queryWrapper.eq(!ObjectUtils.isEmpty(mqttConfig.getQos()),"qos",mqttConfig.getQos());
        queryWrapper.like(!ObjectUtils.isEmpty(mqttConfig.getDefaultTopic()),"default_topic",mqttConfig.getDefaultTopic());
        return ResultJson.ok(mqttConfigService.list(queryWrapper));
    }
}
