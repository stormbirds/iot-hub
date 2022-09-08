package cn.stormbirds.iothub.exception;

import cn.stormbirds.iothub.base.ResultCode;
import cn.stormbirds.iothub.base.ResultJson;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ Description cn.stormbirds.iothub.exception
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/8 23:08
 */
@Configuration
@Order(-2)
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
        if (throwable instanceof BizException) {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
            DataBuffer dataBuffer = null;
            dataBuffer = bufferFactory.wrap( ((BizException) throwable).getResultJson().toString().getBytes());
            serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
        }

        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer dataBuffer = bufferFactory.wrap(
                ResultJson.failure(ResultCode.SERVER_ERROR,
                        throwable.getMessage()).toString().getBytes());
        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}
