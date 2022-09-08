package cn.stormbirds.iothub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {


    @Bean
    public GroupedOpenApi userApi(){
        String[] paths = { "/**" };
        String[] packagedToMatch = { "cn.stormbirds.iothub" };
        return GroupedOpenApi.builder().group("系统模块")
                .pathsToMatch(paths)
//                .addOperationCustomizer((operation, handlerMethod) -> {
//                    return operation.addParametersItem(new HeaderParameter().name("groupCode").example("测试").description("集团code").schema(new StringSchema()._default("BR").name("groupCode").description("集团code")));
//                })
                .packagesToScan(packagedToMatch).build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("StormBirds Iot Hub")
                        .version("1.0")
                        .description( "IOT HUB")
                        .termsOfService("http://blog.stormbirds.cn")
                        .license(new License().name("Apache 2.0")
                                .url("http://blog.stormbirds.cn")));
    }


}