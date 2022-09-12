package cn.stormbirds.iothub.controller;

import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.entity.Device;
import cn.stormbirds.iothub.service.IDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;
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
@RequestMapping("/api/v1/device")
public class DeviceController {
    @Resource
    private IDeviceService deviceService;

    @PostMapping("/save")
    public ResultJson save(@RequestBody Device device){
        return ResultJson.ok(deviceService.save(device));
    }

    @PostMapping("/update")
    public ResultJson update(@RequestBody Device device){
        return ResultJson.ok(deviceService.updateById(device));
    }

    @PostMapping("/delete/{id}")
    public ResultJson delete(@PathVariable Integer id){
        return ResultJson.ok(deviceService.removeById(id));
    }

    @PostMapping("/list")
    public ResultJson list(@RequestBody Device device){
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(device.getId()!=null,"id",device.getId());
        queryWrapper.like(StringUtils.hasText(device.getDeviceName()),"device_name",device.getDeviceName());
        queryWrapper.like(StringUtils.hasText(device.getModel()),"model",device.getModel());
        queryWrapper.like(StringUtils.hasText(device.getDesc()),"desc",device.getDesc());
        return ResultJson.ok(deviceService.list(queryWrapper));
    }
}
