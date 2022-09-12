package cn.stormbirds.iothub.driver;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

/**
 * @ Description cn.stormbirds.iothub.driver.mysql
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/11 23:40
 */
public enum DataBaseTypeEnum {

    /**
     * mysql数据库驱动及连接url模板
     */
    MySQL("mysql", "MySQL", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://{0}:{1}{2}?useUnicode=true&serverTimezone=Asia/Shanghai&characterEncoding={3}"),

    /**
     * Oracle数据库驱动及连接url模板 sid 和 service name两种方式
     */
    Oracle("oracle", "Oracle", "oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@{0}{1}:{2}{3}{4}"),

    /**
     * SqlServer数据库驱动及连接url模板
     */
    SqlServer("sqlserver", "SQLServer", "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                      "jdbc:sqlserver://{0}\\{1}:{2};database={3}"),

    PostgreSQL("postgresql", "PostgreSQL", "org.postgresql.Driver",
                       "jdbc:postgresql://{0}:{1}/{2}?sslmode=disable&useUnicode=true&serverTimezone=Asia/Shanghai&characterEncoding={3}");

    private String name;
    private String code;
    private String driverName;
    private String urlTemplate;
    private static final String ORACLE_SID_CONNECT = "SID";

    DataBaseTypeEnum(String code, String name, String driverName, String urlTemplate) {
        this.code = code;
        this.name = name;
        this.driverName = driverName;
        this.urlTemplate = urlTemplate;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static String getDriverNameByName(String name) {
        String currentDriverName = "";
        for (DataBaseTypeEnum currentEnum : DataBaseTypeEnum.values()) {
            if (StringUtils.equalsIgnoreCase(currentEnum.getName(), name)) {
                currentDriverName = currentEnum.driverName;
                break;
            }
        }
        return currentDriverName;
    }

    public static String getDriverNameByCode(String code) {
        String currentDriverName = "";
        for (DataBaseTypeEnum currentEnum : DataBaseTypeEnum.values()) {
            if (StringUtils.equalsIgnoreCase(currentEnum.getCode(), code)) {
                currentDriverName = currentEnum.driverName;
                break;
            }
        }
        return currentDriverName;
    }

    public static String getUrlByName(String name, String hostName, String port, String databaseName, String conType,
                                      String instanceId, String characterEncoding) {
        String url;
        if (StringUtils.equalsIgnoreCase(SqlServer.getName(), name)) {
            url = MessageFormat.format(SqlServer.urlTemplate, hostName, instanceId, port, databaseName);
        } else if (StringUtils.equalsIgnoreCase(Oracle.getName(), name)) {
            url = getOracleUrl(conType, hostName, port, databaseName);
        } else if(StringUtils.equalsIgnoreCase(PostgreSQL.getName(),name)){
            url = MessageFormat.format(PostgreSQL.urlTemplate, hostName, port, databaseName,
                    StringUtils.isBlank(characterEncoding) ? "utf-8" : characterEncoding);
        }else {
            url = MessageFormat.format(MySQL.urlTemplate, hostName, port, StringUtils.isBlank(databaseName)?"":"/"+databaseName ,
                    StringUtils.isBlank(characterEncoding) ? "utf-8" : characterEncoding);
        }

        return url;
    }

    private static String getOracleUrl(String conType, String hostName, String port, String databaseName) {
        String url;
        if (ORACLE_SID_CONNECT.equalsIgnoreCase(conType)) {
            url = MessageFormat.format(Oracle.urlTemplate, "", hostName, port, ":", databaseName);
            return url;
        }

        url = MessageFormat.format(Oracle.urlTemplate, "//", hostName, port, "/", databaseName);
        return url;
    }
}
