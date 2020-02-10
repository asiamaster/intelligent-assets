package com.dili.ia;


import com.dili.ss.dto.DTOScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.dili.uap.dao", "com.dili.ia.mapper", "com.dili.ss.dao"})
@ComponentScan(basePackages={"com.dili.ss", "com.dili.ia"})
@EnableDiscoveryClient
@EnableFeignClients
@DTOScan(value={"com.dili.ss", "com.dili.ia.domain"})
public class IAApplication  extends SpringBootServletInitializer {

	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(IAApplication.class, args);
	}

}
