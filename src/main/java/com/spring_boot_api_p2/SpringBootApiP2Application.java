package com.spring_boot_api_p2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("test") // <-- Use RequestMapping here
public class SpringBootApiP2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApiP2Application.class, args);
		System.out.println("Spring Boot API developer");
	}

	@GetMapping // <-- This maps to /test
	public String getTest() {
		return "testing";
	}
}



