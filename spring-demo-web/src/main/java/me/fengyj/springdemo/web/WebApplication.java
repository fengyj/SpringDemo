package me.fengyj.springdemo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableFeignClients(basePackages = {"me.fengyj.springdemo.service.share.client"})
public class WebApplication {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(WebApplication.class);

        ApplicationContext ctx = application.run(args);

        System.out.println("==== Beans ====");
        for (String beanName : ctx.getBeanDefinitionNames()) {

            System.out.println(beanName);
        }
        System.out.println("==== Done ====");
    }
}
