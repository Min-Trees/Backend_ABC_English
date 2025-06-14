package com.ABCEnglish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication
@EnableScheduling
public class AbcEnglishApplication {

	public static void main(String[] args) {
		// Load biến môi trường từ file .env
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// Gán các biến môi trường này vào System (nếu cần)
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);
		SpringApplication.run(AbcEnglishApplication.class, args);
	}

}
