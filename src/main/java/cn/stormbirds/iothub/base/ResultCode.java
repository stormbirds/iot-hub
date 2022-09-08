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
