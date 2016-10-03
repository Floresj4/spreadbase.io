package com.flores.h2.spreadful.greeting;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Jason
 */
public class Greeting extends ResourceSupport {

    private final String content;
    
    public Greeting() {
    	this.content = "...";
    }

    public Greeting(@JsonProperty("content") String content) {
    	this.content = content;
    }

	public String getContent() {
		return content;
	}
}