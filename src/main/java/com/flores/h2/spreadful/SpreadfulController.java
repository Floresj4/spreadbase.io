package com.flores.h2.spreadful;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpreadfulController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(SpreadfulController.class);
	
	@RequestMapping
	public HttpEntity<String> spreadful() {
		return new ResponseEntity<String>("testing...", HttpStatus.OK);
	}
}