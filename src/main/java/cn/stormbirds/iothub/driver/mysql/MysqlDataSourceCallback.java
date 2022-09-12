package cn.stormbirds.iothub.driver.mysql;

public interface MysqlDataSourceCallback{
        void connectionDone(String host, Integer port, String scheme, String username,boolean isConnected);
    }