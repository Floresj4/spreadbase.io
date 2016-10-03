package com.flores.h2.spreadful;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String args[]) {
		//initialize logging
		PropertyConfigurator.configure("log4j.properties");
		
		//start
		SpringApplication.run(Application.class, args);
	}
}
