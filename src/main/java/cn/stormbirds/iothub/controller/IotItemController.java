package cn.stormbirds.iothub.controller;

import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.entity.Device;
import cn.stormbirds.iothub.entity.IotItem;
import cn.stormbirds.iothub.service.IIotItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author stormbirds
 * @since 2022-09-13
 */
@RestController
@RequestMapping("/api/v1/iotItem")
public class IotItemController {

    @Resource
    private IIotItemService iotItemService;

    @PostMapping("/save")
    public ResultJson save(@RequestBody IotItem iotItem){
        return ResultJson.ok(iotItemService.save(iotItem));
    }

    @PostMapping("/update")
    public ResultJson update(@RequestBody IotItem iotItem){
        return ResultJson.ok(iotItemService.updateById(iotItem));
    }

    @PostMapping("/delete/{id}")
    public ResultJson delete(@PathVariable Long id){
        return ResultJson.ok(iotItemService.removeById(id));
    }

    @PostMapping("/list")
    public ResultJson list(@RequestBody IotItem iotItem){
        QueryWrapper<IotItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!ObjectUtils.isEmpty(iotItem.getId()),"id",iotItem.getId());
        queryWrapper.eq(!ObjectUtils.isEmpty(iotItem.getEnable()),"enable",iotItem.getEnable());
        queryWrapper.eq(!ObjectUtils.isEmpty(iotItem.getScanRate()),"scan_rate",iotItem.getScanRate());
        return ResultJson.ok(iotItemService.list(queryWrapper));
    }
}
