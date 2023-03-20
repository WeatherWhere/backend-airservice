package com.weatherwhere.airservice;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class AirserviceApplication {

    public static void main(String[] args) throws IOException {

        String rootPath = System.getProperty("user.dir");
        System.out.println(rootPath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> env = objectMapper.readValue(new File(rootPath+"/env.json"), Map.class);

        for (Map.Entry<String, String> entry : env.entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
            System.out.println("환경변수 테스트"+env);
        }
        SpringApplication.run(AirserviceApplication.class, args);
    }

}