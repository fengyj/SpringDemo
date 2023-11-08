package me.fengyj.springdemo.service.share;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class ShareServiceApplication {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(ShareServiceApplication.class);

        ApplicationContext ctx = application.run(args);
    }
}
