package cn.stormbirds.iothub.controller;

import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.entity.Channel;
import cn.stormbirds.iothub.service.IChannelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
@RequestMapping("/api/v1/channel")
public class ChannelController {

    @Resource
    private IChannelService channelService;

    @PostMapping("/save")
    public ResultJson save(@RequestBody Channel channel){
        return ResultJson.ok(channelService.save(channel));
    }

    @PostMapping("/update")
    public ResultJson update(@RequestBody Channel channel){
        return ResultJson.ok(channelService.updateById(channel));
    }

    @PostMapping("/delete/{id}")
    public ResultJson delete(@PathVariable Integer id){
        return ResultJson.ok(channelService.removeById(id));
    }

    @PostMapping("/list")
    public ResultJson list(@RequestBody Channel channel){
        QueryWrapper<Channel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(channel.getId()!=null,"id",channel.getId());
        queryWrapper.like(StringUtils.hasText(channel.getChannelName()),"channel_name",channel.getChannelName());
        queryWrapper.like(StringUtils.hasText(channel.getDriver()),"driver",channel.getDriver());
        queryWrapper.like(StringUtils.hasText(channel.getDesc()),"desc",channel.getDesc());
        return ResultJson.ok(channelService.list(queryWrapper));
    }
}
