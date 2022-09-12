package cn.stormbirds.iothub.base;

/**
 * @ Description cn.stormbirds.iothub.base
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/8 23:11
 */
public enum ResultCode {
    SUCCESS(0, "成功"),

    BAD_REQUEST(40000, "参数错误"),
    UNAUTHORIZED(40100, "未授权，或者权限不足"),
    SERVER_ERROR(50000, "服务器内部错误"),

    TABLE_NOT_EXIST(21029, "数据库表不存在，请重新绑定数据源!"),
    COLUMN_NOT_EXIST(21030, "数据表字段不存在，请检查语句或者数据库表!"),
    SCRIPT_ERROR(21031, "SQL语句错误，请重新检查！"),
    SQL_ERROR(21032, "SQL查询出错，未知原因！"),
    SQL_CONNECTION_ERROR(21033, "SQL连接信息出错，请检查连接信息！"),

    MQTT_SENDMESSAGE_ERROR(31000,"MQTT发送消息出错"),
    MQTT_STOP_ERROR(31001,"MQTT服务停止失败"),
    ;
    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
