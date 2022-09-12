package cn.stormbirds.iothub.driver;

import cn.stormbirds.iothub.base.ResultCode;
import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.driver.mysql.MysqlDataSourceCallback;
import cn.stormbirds.iothub.exception.BizException;
import cn.stormbirds.iothub.service.impl.MysqlConfigServiceImpl;
import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.util.MySqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ Description cn.stormbirds.iothub.driver.mysql
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/11 23:01
 */
@Slf4j
@Component
public class DruidPoolUtils {
    private final Map<String, DruidDataSource> druidPools = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private MysqlDataSourceCallback mysqlDataSourceCallback;

    @Value("${spring.datasource.druid.filters:stat,slf4j,config}")
    private String druidFilters;
    @Value("${spring.datasource.druid.maxActive:50}")
    private String druidMaxActive;
    @Value("${spring.datasource.druid.initialSize:8}")
    private String druidinitialSize;
    @Value("${spring.datasource.druid.maxWait:30000}")
    private String druidmaxWait;
    @Value("${spring.datasource.druid.minIdle:1}")
    private String druidminIdle;
    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis:60000}")
    private String druidtimeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis:300000}")
    private String druidminEvictableIdleTimeMillis;
    @Value("${spring.datasource.druid.validationQuery:SELECT 'x'}")
    private String druidvalidationQuery;
    @Value("${spring.datasource.druid.testWhileIdle:true}")
    private String druidtestWhileIdle;
    @Value("${spring.datasource.druid.testOnBorrow:false}")
    private String druidtestOnBorrow;
    @Value("${spring.datasource.druid.testOnReturn:false}")
    private String druidtestOnReturn;
    @Value("${spring.datasource.druid.poolPreparedStatements:true}")
    private String druidpoolPreparedStatements;
    @Value("${spring.datasource.druid.maxPoolPreparedStatementPerConnectionSize:20}")
    private String druidmaxPoolPreparedStatementPerConnectionSize;

    public DruidPooledConnection getConnection(String dbType, String host, Integer port, String dbName, String username, String password) throws SQLException{
        return getConnection(dbType,host,port,dbName,username,password,true);
    }

    public DruidPooledConnection getConnection(String dbType, String host, Integer port, String dbName, String username, String password, boolean enableCache) throws SQLException {

        String url = DataBaseTypeEnum.getUrlByName(dbType,host,port.toString(),dbName,null,null,null);

        DruidPooledConnection connection = null;
        lock.readLock().lock();
        if (druidPools.get(url + username) == null) {
            lock.readLock().unlock();

            Properties dbProperties = new Properties();
            dbProperties.setProperty("driverClassName", JdbcUtils.getDriverClassName(url));
            dbProperties.setProperty("url", url);
            dbProperties.setProperty("username", username);
            dbProperties.setProperty("password", password);
            dbProperties.setProperty("filters", druidFilters);
            dbProperties.setProperty("maxActive", druidMaxActive);// 最大连接数
            dbProperties.setProperty("minIdle", druidminIdle);// 最小空闲数
            dbProperties.setProperty("initialSize", druidinitialSize);// 连接池初始连接数
            dbProperties.setProperty("maxWait", druidmaxWait);// 等待空闲最小时间//超时回收时间(以毫秒为单位)
            dbProperties.setProperty("testOnBorrow", druidtestOnBorrow);
            dbProperties.setProperty("testOnReturn", druidtestOnReturn);
            dbProperties.setProperty("timeBetweenEvictionRunsMillis", druidtimeBetweenEvictionRunsMillis);
            dbProperties.setProperty("minEvictableIdleTimeMillis", druidminEvictableIdleTimeMillis);
            dbProperties.setProperty("testWhileIdle", druidtestWhileIdle);
            dbProperties.setProperty("connectionErrorRetryAttempts", "5");
            dbProperties.setProperty("breakAfterAcquireFailure", "false");
            dbProperties.setProperty("poolPreparedStatements", druidpoolPreparedStatements);
            dbProperties.setProperty("maxPoolPreparedStatementPerConnectionSize", druidmaxPoolPreparedStatementPerConnectionSize);
            dbProperties.setProperty("validationQuery", getValidationQuery(dbType));

//            dbProperties.setProperty("connectionProperties", "config.decrypt=true;config.decrypt.key=${publickey}");
            DruidDataSource druidDataSource;
            lock.writeLock().lock();
            try {
                if (druidPools.get(url + username) == null) {
                    druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(dbProperties);
                    druidDataSource.setConnectionErrorRetryAttempts(3);
                    druidDataSource.setBreakAfterAcquireFailure(false);
                    druidDataSource.setPoolPreparedStatements(false);
                    druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(0);
                    //是否自动回收超时连接
                    druidDataSource.setRemoveAbandoned(true);
                    //回收超时连接超时时间(以秒数为单位)
                    druidDataSource.setRemoveAbandonedTimeout(30);
                    druidPools.put(url + username, druidDataSource);
                }
            } catch (Exception e) {
                this.mysqlDataSourceCallback.connectionDone(host,port,dbName,username,false);
                throw new RuntimeException(e);
            } finally {
                lock.writeLock().unlock();
            }

        }else{
            lock.readLock().unlock();
        }

        lock.readLock().lock();
        try {
            connection = druidPools.get(url + username).getConnection();
            this.mysqlDataSourceCallback.connectionDone(host,port,dbName,username,true);
            log.debug("当前{}连接数 {}",url, druidPools.get(url + username).getActiveCount());
        } catch (SQLException throwables) {
            this.mysqlDataSourceCallback.connectionDone(host,port,dbName,username,false);
            throw new RuntimeException(throwables);
        } finally {
            lock.readLock().unlock();
        }

        return connection;

    }

    public boolean removeConnection(String dbType, String host, Integer port, String dbName, String username){
        String url = DataBaseTypeEnum.getUrlByName(dbType,host,port.toString(),dbName,null,null,null);
        DruidDataSource dataSource = druidPools.get(url + username);
        if(dataSource!=null){
            druidPools.remove(url + username).close();
            this.mysqlDataSourceCallback.connectionDone(host,port,dbName,username,false);
        }
        return true;
    }

    public boolean testConnection(String dbType, String host, Integer port, String dbName, String username, String password) {

        try {
            Future<Connection> connectionFuture = Executors.newSingleThreadExecutor().submit(new Callable<Connection>() {
                @Override
                public Connection call() throws Exception {
                    Connection connection = null;
                    // 注册 JDBC 驱动
                    try {
                        String url = DataBaseTypeEnum.getUrlByName(dbType,host,port.toString(),"",null,null,null);
                        Class.forName(DataBaseTypeEnum.getDriverNameByName(dbType));
                        connection = DriverManager.getConnection(url,username,password);
                        return connection;
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }finally {
                        if(connection!=null) {
                            try {
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return null;
                }
            });
            return connectionFuture.get(5, TimeUnit.SECONDS)!=null;
        } catch (Exception e) {
            log.error("连接信息有误",e);
        }finally {
        }
        return false;
    }

    public List<String> showTables(String dbType, String host, Integer port, String dbName, String username, String password) throws BizException {
        try {
            Future<List<String>> connectionFuture = Executors.newSingleThreadExecutor().submit(new Callable<List<String>>() {
                @Override
                public List<String> call() throws Exception {
                    Connection connection = null;
                    // 注册 JDBC 驱动
                    try {
                        String url = DataBaseTypeEnum.getUrlByName(dbType,host,port.toString(),dbName,null,null,null);
                        Class.forName(DataBaseTypeEnum.getDriverNameByName(dbType));
                        connection = DriverManager.getConnection(url,username,password);
                        return MySqlUtils.showTables(connection);
                    } finally {
                        if(connection!=null) {
                            try {
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            return connectionFuture.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new BizException(new ResultJson(ResultCode.SQL_CONNECTION_ERROR, e.getMessage()));
        }
    }

    public List<String> showDatabases(String dbType, String host, Integer port, String dbName, String username, String password) throws BizException {
        try {
            Future<List<String>> connectionFuture = Executors.newSingleThreadExecutor().submit(new Callable<List<String>>() {
                @Override
                public List<String> call() throws Exception {
                    Connection connection = null;
                    // 注册 JDBC 驱动
                    try {
                        String url = DataBaseTypeEnum.getUrlByName(dbType,host,port.toString(),dbName,null,null,null);
                        Class.forName(DataBaseTypeEnum.getDriverNameByName(dbType));
                        connection = DriverManager.getConnection(url,username,password);
                        return showDatabases(connection);
                    } finally {
                        if(connection!=null) {
                            try {
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            return connectionFuture.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new BizException(new ResultJson(ResultCode.SQL_CONNECTION_ERROR, e.getMessage()));
        }
    }

    private static List<String> showDatabases(Connection conn) throws SQLException {
        List<String> tables = new ArrayList<String>();

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SHOW DATABASES");
            while (rs.next()) {
                String tableName = rs.getString(1);
                tables.add(tableName);
            }
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }

        return tables;
    }

    private String getValidationQuery(String driver) {
        String validationQuery;
        if ("oracle".equalsIgnoreCase(driver)) {
            // Oracle
            validationQuery = "select 1 from dual";
        } else {
            // mysql,sqlServer
            validationQuery = druidvalidationQuery;
        }
        return validationQuery;
    }

    public List<Map<String, Object>> getListDataBySQL(DruidPooledConnection conn, String sql, Object... params) throws BizException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            // 执行时去除sql中的";" ，避免oracle数据库执行失败
            if (sql.contains(";")) {
                ps = conn.prepareStatement(sql.substring(0, sql.indexOf(";")));
            } else {
                ps = conn.prepareStatement(sql);
            }
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            ps.setQueryTimeout(60);
            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<>(16);
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    int type = rsmd.getColumnType(i + 1);
                    Object colValue;
                    switch (type) {
                        case Types.TIMESTAMP: // 如果是时间戳类型
                        case Types.DATE: // 如果是时间类型

                            colValue = rs.getTimestamp(i + 1) != null ? rs.getTimestamp(i + 1).getTime() : null;
                            break;
                        default:
                            colValue = rs.getObject(i + 1);
                            break;
                    }
                    String colName = rsmd.getColumnLabel(i + 1);
                    map.put(colName, colValue);
                }
                mapList.add(map);
            }
            return mapList;
        } catch (SQLException e) {
            log.error("----- SQL语句 -----\n" + sql + "\n数据库查询出错：\n", e);
            String errMsg = e.getMessage();
            //表不存在
            if (errMsg.matches("(.*)doesn't exist(.*)")) {
                throw new BizException(new ResultJson(ResultCode.TABLE_NOT_EXIST, ResultCode.TABLE_NOT_EXIST.getMsg()));
            }
            //相同表名，没有字段
            if (errMsg.matches("(.*)Unknown column(.*)")) {
                throw new BizException(new ResultJson(ResultCode.COLUMN_NOT_EXIST, ResultCode.COLUMN_NOT_EXIST.getMsg()));
            }
            //语法错误
            if (errMsg.matches("(.*)syntax error(.*)")) {
                throw new BizException(new ResultJson(ResultCode.SCRIPT_ERROR, ResultCode.SCRIPT_ERROR.getMsg()));
            }
            throw new BizException(new ResultJson(ResultCode.SQL_ERROR, errMsg));
        } finally {

        }
    }

    public void setMysqlCallback(MysqlDataSourceCallback mysqlDataSourceCallback) {
        this.mysqlDataSourceCallback = mysqlDataSourceCallback;
    }
}
