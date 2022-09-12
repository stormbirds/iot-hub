package cn.stormbirds.iothub.service.impl;

import cn.stormbirds.iothub.entity.Channel;
import cn.stormbirds.iothub.mapper.ChannelMapper;
import cn.stormbirds.iothub.service.IChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stormbirds
 * @since 2022-09-11
 */
@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, Channel> implements IChannelService {

}
