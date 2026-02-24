package com.base.cms.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.base.cms.user", "com.base.cms.common"})
@EntityScan(basePackages = "com.base.cms.common.entities")
public class BaseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseServiceApplication.class, args);
	}
}
