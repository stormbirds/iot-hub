package cn.stormbirds.iothub.service;

import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.entity.MysqlConfig;
import cn.stormbirds.iothub.exception.BizException;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stormbirds
 * @since 2022-09-11
 */
public interface IMysqlConfigService extends IService<MysqlConfig> {

    boolean edit(MysqlConfig mysqlConfig);

    boolean start(Integer id);

    boolean stop(Integer id);

    ResultJson testConnection(MysqlConfig mysqlConfig) throws BizException;

    ResultJson showTables(MysqlConfig mysqlConfig) throws BizException;

    ResultJson showDatabases(MysqlConfig mysqlConfig);
}
