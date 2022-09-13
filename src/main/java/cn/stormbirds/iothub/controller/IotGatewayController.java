package cn.stormbirds.iothub.controller;

import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.service.IIotgatewayService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ Description cn.stormbirds.iothub.controller
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/13 0:04
 */
@RequestMapping("/iotgateway")
@RestController("")
public class IotGatewayController {

    @Resource
    private IIotgatewayService iotgatewayService;



    @GetMapping("/list")
    public ResultJson list(){
        return ResultJson.ok(iotgatewayService.list());
    }

    @PostMapping("/delete/{id}")
    public ResultJson delete(@PathVariable Long id){
        return ResultJson.ok(iotgatewayService.removeById(id));
    }

    @PostMapping("/toggleSwitch/{id}")
    public ResultJson toggleSwitch(@PathVariable Long id){
        return ResultJson.ok(iotgatewayService.toggleSwitch(id));
    }
}
