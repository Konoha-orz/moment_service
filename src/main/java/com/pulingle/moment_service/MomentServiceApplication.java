package com.pulingle.moment_service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@MapperScan(basePackages = "com.pulingle.moment_service.mapper")
public class MomentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MomentServiceApplication.class, args);
	}
}
