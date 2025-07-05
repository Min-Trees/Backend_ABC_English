package com.ABCEnglish.configuration;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ABC English API")
                        .version("1.0.0")
                        .description("API quản lý thư viện, bài tập, câu hỏi")
                        .contact(new Contact()
                                .name("Nhóm phát triển")
                                .email("abc@english.com")));
    }
}
