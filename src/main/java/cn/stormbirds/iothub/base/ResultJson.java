package cn.stormbirds.iothub.base;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * @ Description cn.stormbirds.iothub.base
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/8 23:10
 */
public class ResultJson<T> implements Serializable {


    private static final long serialVersionUID = 7488995406331282790L;
    @Schema(description = "结果状态码")
    private int code;
    @Schema(description = "结果消息")
    private String msg;
    @Schema(description = "结果数据内容")
    private T data;

    public static ResultJson ok() {
        return ok("");
    }

    public static ResultJson ok(Object o) {
        return new ResultJson(ResultCode.SUCCESS, o);
    }

    public static ResultJson failure(ResultCode code) {
        return failure(code, code.getMsg());
    }

    public static ResultJson failure(ResultCode code, Object o) {
        return new ResultJson(code, o);
    }

    public ResultJson (ResultCode resultCode) {
        setResultCode(resultCode);
    }

    public ResultJson (ResultCode resultCode,T data) {
        setResultCode(resultCode);
        this.data = data;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"msg\":\"" + msg + '\"' +
                ", \"data\":\"" + data + '\"'+
                '}';
    }
}


