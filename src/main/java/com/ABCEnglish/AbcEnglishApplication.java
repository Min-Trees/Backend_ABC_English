package com.ABCEnglish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AbcEnglishApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbcEnglishApplication.class, args);
	}

}
