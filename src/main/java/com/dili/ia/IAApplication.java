package com.dili.ia;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.dili.ss", "com.dili.ia"})
public class IAApplication {

	public static void main(String[] args) {
		SpringApplication.run(IAApplication.class, args);
	}

}
