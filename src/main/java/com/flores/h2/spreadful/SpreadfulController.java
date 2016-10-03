package com.flores.h2.spreadful;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpreadfulController {

	private static final Logger logger = LoggerFactory.getLogger(SpreadfulController.class);

	/**
	 * put or post
	 * @param o something to test post
	 * @return
	 */
	@RequestMapping(value = "/test")
	public HttpEntity<Object> test() {
		return new ResponseEntity<Object>("testing...", HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/load")
	public void load() {
		String filename = "unset-filename.xlsx";
		logger.debug("loading {}", filename);
	}
}