package com.dili.ia;


import com.dili.ss.dto.DTOScan;
import com.dili.ss.retrofitful.annotation.RestfulScan;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients(basePackages = {"com.dili.ia.rpc", "com.dili.bpmc.sdk.rpc.feign", "com.dili.logger.sdk.rpc", "com.dili.assets.sdk.rpc", "com.dili.rule.sdk.rpc","com.dili.settlement.rpc","com.dili.customer.sdk.rpc","com.dili.uid.sdk.rpc.feign"})
@MapperScan(basePackages = { "com.dili.ia.mapper", "com.dili.ss.dao"})
@ComponentScan(basePackages={"com.dili.ss", "com.dili.ia", "com.dili.uap.sdk", "com.dili.logger.sdk", "com.dili.assets.sdk","com.dili.rule.sdk", "com.dili.bpmc.sdk.aop","com.dili.commons","com.dili.customer.sdk"})
@RestfulScan({"com.dili.ia.rpc","com.dili.uap.sdk.rpc", "com.dili.bpmc.sdk.rpc.restful"})
@DTOScan(value={"com.dili.ss", "com.dili.ia.domain", "com.dili.uap.sdk.domain", "com.dili.bpmc.sdk"})
public class IAApplication  extends SpringBootServletInitializer {

    //	@Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(IAApplication.class, args);
    }


}
