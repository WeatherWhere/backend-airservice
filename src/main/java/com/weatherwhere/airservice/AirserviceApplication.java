package com.weatherwhere.airservice;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaAuditing
public class AirserviceApplication {

    public static void main(String[] args) throws IOException {

        String rootPath = System.getProperty("user.dir");
        System.out.println(rootPath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> env = objectMapper.readValue(new File(rootPath+"/env.json"), Map.class);

        for (Map.Entry<String, String> entry : env.entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
           // System.out.println("환경변수 테스트"+env);
        }
        SpringApplication.run(AirserviceApplication.class, args);
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000","https://dpezzthgwnnvc.cloudfront.net","https://m.weatherwhere.link");
            }
        };
    }

}