package cn.stormbirds.iothub.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.stormbirds.iothub.base.ResultCode;
import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.driver.DataBaseTypeEnum;
import cn.stormbirds.iothub.driver.DruidPoolUtils;
import cn.stormbirds.iothub.driver.mysql.DataBaseSourceCallback;
import cn.stormbirds.iothub.entity.MysqlConfig;
import cn.stormbirds.iothub.exception.BizException;
import cn.stormbirds.iothub.mapper.MysqlConfigMapper;
import cn.stormbirds.iothub.mqtt.MqttConstant;
import cn.stormbirds.iothub.service.IMysqlConfigService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stormbirds
 * @since 2022-09-11
 */
@Service
public class ConfigServiceImplBase extends ServiceImpl<MysqlConfigMapper, MysqlConfig> implements IMysqlConfigService , DataBaseSourceCallback {

    @Resource
    private DruidPoolUtils druidPoolUtils;

    @PostConstruct
    public void init(){
        druidPoolUtils.setDataBaseCallback(this);
    }

    @Override
    public boolean edit(MysqlConfig mysqlConfig) {
        mysqlConfig.setUpdateAt(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_FORMATTER));
        return updateById(mysqlConfig);
    }

    @Override
    public boolean start(Long id) {

        MysqlConfig mysqlConfig = getById(id);
        if(mysqlConfig.getEnable()) {
            return true;
        }
        mysqlConfig.setEnable(true);
        try {
            druidPoolUtils.getConnection(DataBaseTypeEnum.MySQL.getCode(),
                    mysqlConfig.getHost(),mysqlConfig.getPort(),mysqlConfig.getScheme(),mysqlConfig.getUsername(),mysqlConfig.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateById(mysqlConfig);
    }

    @Override
    public boolean stop(Long id) {
        MysqlConfig mysqlConfig = getById(id);
        if(!mysqlConfig.getEnable()) {
            return true;
        }
        mysqlConfig.setEnable(false);
        return updateById(mysqlConfig) && druidPoolUtils.removeConnection(DataBaseTypeEnum.MySQL.getCode(),
                mysqlConfig.getHost(),mysqlConfig.getPort(),mysqlConfig.getScheme(),mysqlConfig.getUsername());
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
        return ResultJson.ok(druidPoolUtils.showTables("mysql",
                mysqlConfig.getHost(),
                mysqlConfig.getPort(),
                mysqlConfig.getScheme(),
                mysqlConfig.getUsername(),
                mysqlConfig.getPassword()));
    }

    @Override
    public ResultJson showDatabases(MysqlConfig mysqlConfig) {
        return ResultJson.ok(druidPoolUtils.showDatabases("mysql",
                mysqlConfig.getHost(),
                mysqlConfig.getPort(),
                mysqlConfig.getScheme(),
                mysqlConfig.getUsername(),
                mysqlConfig.getPassword()));
    }

    @Override
    public void connectionDone(String host, Integer port, String scheme, String username,boolean isConnected) {
        updateBatchById(list(Wrappers.<MysqlConfig>lambdaQuery()
                .eq(MysqlConfig::getHost,host)
                .eq(MysqlConfig::getPort,port)
                .eq(MysqlConfig::getScheme,scheme)
                .eq(MysqlConfig::getUsername,username)
        ).stream().peek(mysqlConfig -> mysqlConfig.setStatus(isConnected?MqttConstant.ONLINE:MqttConstant.OFFLINE)).collect(Collectors.toList()));
    }

    @Override
    public void listByDriver(Integer id, List<Map<String, Object>> result) {

    }

}
