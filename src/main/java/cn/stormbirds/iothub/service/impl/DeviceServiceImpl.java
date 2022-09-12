package cn.stormbirds.iothub.service.impl;

import cn.stormbirds.iothub.entity.Device;
import cn.stormbirds.iothub.mapper.DeviceMapper;
import cn.stormbirds.iothub.service.IDeviceService;
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
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

}
