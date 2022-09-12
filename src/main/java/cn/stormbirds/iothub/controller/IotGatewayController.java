package cn.stormbirds.iothub.controller;

import cn.stormbirds.iothub.base.ResultJson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Description cn.stormbirds.iothub.controller
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/13 0:04
 */
@RequestMapping("/iotgateway")
@RestController("")
public class IotGatewayController {

    @GetMapping("/list")
    public ResultJson list(){
        return ResultJson.ok();
    }
}
