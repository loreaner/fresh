package com.fresh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.fresh.core.mapper")
@EnableTransactionManagement
public class FreshDeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(FreshDeliveryApplication.class, args);
    }
}