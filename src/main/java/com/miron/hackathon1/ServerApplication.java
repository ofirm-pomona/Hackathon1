package com.miron.hackathon1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {

	// Main
	public static void main(String[] args) throws Exception {
		// Run Spring Boot application
		SpringApplication.run(ServerApplication.class, args);
	}

	// Called when application is started
	public void run(String... args) throws Exception {
        System.out.println("Server application started...");
	}
}