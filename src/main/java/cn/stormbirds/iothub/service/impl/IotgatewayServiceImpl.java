package cn.stormbirds.iothub.service.impl;

import cn.stormbirds.iothub.base.IotGateWayConstant;
import cn.stormbirds.iothub.base.ResultCode;
import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.entity.Iotgateway;
import cn.stormbirds.iothub.entity.MqttConfig;
import cn.stormbirds.iothub.exception.BizException;
import cn.stormbirds.iothub.mapper.IotgatewayMapper;
import cn.stormbirds.iothub.mqtt.MqttConstant;
import cn.stormbirds.iothub.service.IIotgatewayService;
import cn.stormbirds.iothub.service.IMqttConfigService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author stormbirds
 * @since 2022-09-13
 */
@Service
public class IotgatewayServiceImpl extends ServiceImpl<IotgatewayMapper, Iotgateway> implements IIotgatewayService {

    @Resource
    private IMqttConfigService mqttConfigService;

    @Override
    public boolean toggleSwitch(Long id) {
        Iotgateway iotgateway = getById(id);
        if (iotgateway == null) throw new BizException(ResultJson.failure(ResultCode.RESOURCE_NOT_FOUND));
        switch (iotgateway.getType()) {
            case IotGateWayConstant
                    .MQTT_CLIENT:
                MqttConfig mqttConfig = mqttConfigService.getById(iotgateway.getAgentId());
            if(iotgateway.getEnable()){
                update(Wrappers.<Iotgateway>lambdaUpdate().set(Iotgateway::getEnable,false));
                if (MqttConstant.ONLINE.equalsIgnoreCase(mqttConfig.getStatus())) {
                    mqttConfigService.justStopService(iotgateway.getAgentId());
                }
            }else{
                update(Wrappers.<Iotgateway>lambdaUpdate().set(Iotgateway::getEnable,true));
                if (MqttConstant.OFFLINE.equalsIgnoreCase(mqttConfig.getStatus()) && mqttConfig.getEnable()) {
                    mqttConfigService.start(iotgateway.getAgentId());
                }
            }
                break;
            case IotGateWayConstant.REST_CLIENT:
                break;
            case IotGateWayConstant.REST_SERVER:
                break;
            case IotGateWayConstant.THING_WORX:
                break;
            default:
                throw new BizException(ResultJson.failure(ResultCode.IOT_GATEWAY_TYPE_ERROR));
        }
        return false;
    }

    @Override
    public void startByAgentId(Long id) {
        Iotgateway iotgateway = getById(id);
        if (iotgateway == null) throw new BizException(ResultJson.failure(ResultCode.RESOURCE_NOT_FOUND));
        switch (iotgateway.getType()) {
            case IotGateWayConstant
                    .MQTT_CLIENT:
                if(iotgateway.getEnable()){
                    mqttConfigService.start(iotgateway.getAgentId());
                }
                break;
            case IotGateWayConstant.REST_CLIENT:
                break;
            case IotGateWayConstant.REST_SERVER:
                break;
            case IotGateWayConstant.THING_WORX:
                break;
            default:
                throw new BizException(ResultJson.failure(ResultCode.IOT_GATEWAY_TYPE_ERROR));
        }
    }

    @Override
    public boolean removeById(Serializable id) {
        Iotgateway iotgateway = getById(id);
        if (iotgateway == null) return true;

        switch (iotgateway.getType()) {
            case IotGateWayConstant
                    .MQTT_CLIENT:
                MqttConfig mqttConfig = mqttConfigService.getById(iotgateway.getAgentId());
                if (MqttConstant.ONLINE.equalsIgnoreCase(mqttConfig.getStatus())) {
                    mqttConfigService.stop(iotgateway.getAgentId());
                }
                mqttConfigService.removeById(iotgateway.getAgentId());
                break;
            case IotGateWayConstant.REST_CLIENT:
                break;
            case IotGateWayConstant.REST_SERVER:
                break;
            case IotGateWayConstant.THING_WORX:
                break;
            default:
                throw new BizException(ResultJson.failure(ResultCode.IOT_GATEWAY_TYPE_ERROR));
        }
        return super.removeById(id);
    }
}
