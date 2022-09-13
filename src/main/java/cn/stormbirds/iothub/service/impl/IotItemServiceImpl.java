package cn.stormbirds.iothub.service.impl;

import cn.stormbirds.iothub.entity.IotItem;
import cn.stormbirds.iothub.entity.Iotgateway;
import cn.stormbirds.iothub.iotgateway.IotItemTimedTaskTimer;
import cn.stormbirds.iothub.mapper.IotItemMapper;
import cn.stormbirds.iothub.service.IIotItemService;
import cn.stormbirds.iothub.service.IIotgatewayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stormbirds
 * @since 2022-09-13
 */
@Service
public class IotItemServiceImpl extends ServiceImpl<IotItemMapper, IotItem> implements IIotItemService {

    ScheduledExecutorService service = new ScheduledThreadPoolExecutor(20,
            new BasicThreadFactory.Builder().namingPattern("IotItem-schedule-pool-%d").daemon(true).build());;

    @Resource
    private IIotgatewayService iotgatewayService;

    @Override
    public boolean save(IotItem entity) {
        if(entity.getEnable()){
            service.scheduleWithFixedDelay();
        }
        return super.save(entity);
    }

    @Override
    public boolean updateById(IotItem entity) {
        return super.updateById(entity);
    }
}
