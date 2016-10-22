package com.flores.h2.spreadbaseio;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String args[]) {
		// initialize logging and run
		PropertyConfigurator.configure("src/test/resources/log4j.properties");
		SpringApplication.run(Application.class, args);
	}
}
