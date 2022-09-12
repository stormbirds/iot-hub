package cn.stormbirds.iothub.exception;

import cn.stormbirds.iothub.base.ResultJson;

/**
 * @ Description cn.stormbirds.iothub.exception
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/8 23:09
 */
public class BizException extends RuntimeException{
    private static final long serialVersionUID = 8344304071610594677L;
    private ResultJson resultJson;

    public BizException(ResultJson resultJson) {
        this.resultJson = resultJson;;
    }

    public ResultJson getResultJson(){
        return resultJson;
    }
}
