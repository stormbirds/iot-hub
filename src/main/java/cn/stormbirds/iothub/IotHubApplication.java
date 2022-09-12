package cn.stormbirds.iothub;

//import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableAdminServer
@SpringBootApplication
@MapperScan("cn.stormbirds.iothub.mapper")
public class IotHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotHubApplication.class, args);
    }

}
