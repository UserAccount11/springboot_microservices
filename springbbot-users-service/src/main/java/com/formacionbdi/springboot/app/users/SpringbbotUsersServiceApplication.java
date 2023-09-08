package com.formacionbdi.springboot.app.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({ "com.formacionbdi.springboot.app.commons.users.models.entity" })
public class SpringbbotUsersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbbotUsersServiceApplication.class, args);
	}

}
