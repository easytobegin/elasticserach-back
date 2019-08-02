package com.panport.logCloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by minisheep on 2019/7/12.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class ApplicationRunner {
    public static void main(String[] args) {

        SpringApplication.run(ApplicationRunner.class,args);
    }
}
