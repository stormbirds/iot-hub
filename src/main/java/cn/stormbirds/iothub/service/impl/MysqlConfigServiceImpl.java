package cn.stormbirds.iothub.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.stormbirds.iothub.base.ResultCode;
import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.driver.DruidPoolUtils;
import cn.stormbirds.iothub.entity.MqttConfig;
import cn.stormbirds.iothub.entity.MysqlConfig;
import cn.stormbirds.iothub.exception.BizException;
import cn.stormbirds.iothub.mapper.MysqlConfigMapper;
import cn.stormbirds.iothub.service.IMysqlConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stormbirds
 * @since 2022-09-11
 */
@Service
public class MysqlConfigServiceImpl extends ServiceImpl<MysqlConfigMapper, MysqlConfig> implements IMysqlConfigService {

    @Resource
    private DruidPoolUtils druidPoolUtils;

    @Override
    public boolean edit(MysqlConfig mysqlConfig) {
        mysqlConfig.setUpdateAt(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_FORMATTER));
        return updateById(mysqlConfig);
    }

    @Override
    public boolean start(Integer id) {

        MysqlConfig mysqlConfig = getById(id);
        if(mysqlConfig.getEnable()) return true;
        mysqlConfig.setEnable(true);
        return false;
    }

    @Override
    public boolean stop(Integer id) {
        return false;
    }

    @Override
    public ResultJson testConnection(MysqlConfig mysqlConfig) throws BizException {
        if(druidPoolUtils.testConnection("mysql",mysqlConfig.getHost(),mysqlConfig.getPort(),mysqlConfig.getScheme(),mysqlConfig.getUsername(),mysqlConfig.getPassword())){
            return ResultJson.ok();
        }
        return ResultJson.failure(ResultCode.SQL_CONNECTION_ERROR);
    }

    @Override
    public ResultJson showTables(MysqlConfig mysqlConfig)  {
        return ResultJson.ok(druidPoolUtils.showTables("mysql",mysqlConfig.getHost(),mysqlConfig.getPort(),mysqlConfig.getScheme(),mysqlConfig.getUsername(),mysqlConfig.getPassword()));
    }

    @Override
    public ResultJson showDatabases(MysqlConfig mysqlConfig) {
        return ResultJson.ok(druidPoolUtils.showDatabases("mysql",mysqlConfig.getHost(),mysqlConfig.getPort(),mysqlConfig.getScheme(),mysqlConfig.getUsername(),mysqlConfig.getPassword()));
    }
}
