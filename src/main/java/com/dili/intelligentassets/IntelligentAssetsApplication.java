package com.dili.intelligentassets;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = {"com.dili.customer.mapper", "com.dili.ss.dao"})
@SpringBootApplication
@ComponentScan(basePackages={"com.dili.ss","com.dili.intelligentassets"})
public class IntelligentAssetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntelligentAssetsApplication.class, args);
	}

}
