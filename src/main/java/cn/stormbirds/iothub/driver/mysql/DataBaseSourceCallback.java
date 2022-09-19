package cn.stormbirds.iothub.driver.mysql;

import java.util.List;
import java.util.Map;

public interface DataBaseSourceCallback {
    /**
     * 回调ODBC驱动连接状态
     * @param host
     * @param port
     * @param scheme
     * @param username
     * @param isConnected 是否已连接
     */
    void connectionDone(String host, Integer port, String scheme, String username,boolean isConnected);

    /**
     * 返回基于关系型数据库的设备结果
     * @param id 设备ID
     * @param result 设备结果
     */
    void listByDriver(Integer id, List<Map<String, Object>> result);
    }