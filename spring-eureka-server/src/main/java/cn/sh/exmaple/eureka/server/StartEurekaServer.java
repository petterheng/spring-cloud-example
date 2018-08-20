package cn.sh.exmaple.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author sh
 */
@EnableEurekaServer
@SpringBootApplication
public class StartEurekaServer {

    public static void main(String[] args) {
        SpringApplication.run(StartEurekaServer.class, args);
    }
}
