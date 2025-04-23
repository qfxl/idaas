package com.zbycorp.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuyonghong
 * @date 2025-04-08 16:34
 **/
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.zbycorp"})
public class Application {
    public static void main(String[] args) {
        log.info("Begin to start Spring Boot Application");
        long startTime = System.currentTimeMillis();

        SpringApplication.run(Application.class, args);

        long endTime = System.currentTimeMillis();
        log.info("End starting Spring Boot Application, Time used: " + (endTime - startTime) + "ms");
    }
}
