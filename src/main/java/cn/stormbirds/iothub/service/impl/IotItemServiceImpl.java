package cn.stormbirds.iothub.service.impl;

import cn.stormbirds.iothub.entity.IotItem;
import cn.stormbirds.iothub.mapper.IotItemMapper;
import cn.stormbirds.iothub.service.IIotItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

    @Override
    public boolean updateById(IotItem entity) {
        return super.updateById(entity);
    }
}
