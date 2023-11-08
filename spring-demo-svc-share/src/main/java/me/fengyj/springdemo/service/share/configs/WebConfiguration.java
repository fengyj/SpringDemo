package me.fengyj.springdemo.service.share.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("me.fengyj.springdemo.service.share")
public class WebConfiguration implements WebMvcConfigurer {
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//
//        registry.addRedirectViewController("/", "/swagger-ui/index.html");
//    }
//
//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/images/**").addResourceLocations("classpath:static/images/");
//        registry.addResourceHandler("/static/css/**").addResourceLocations("classpath:static/css/");
//        registry.addResourceHandler("/static/js/**").addResourceLocations("classpath:static/js/");
//    }
}
