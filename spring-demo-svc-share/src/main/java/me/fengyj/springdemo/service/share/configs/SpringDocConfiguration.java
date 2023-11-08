package me.fengyj.springdemo.service.share.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Spring Demo - Share APIs", version = "0.1"))
public class SpringDocConfiguration {

    @Bean
    public GroupedOpenApi shareApi() {
        return GroupedOpenApi.builder()
                             .group("spring-demo-share-service")
                             .pathsToMatch("/share/**")
                             .packagesToScan("me.fengyj.springdemo.service.share.controllers")
                             .build();
    }
}
