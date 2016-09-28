package com.flores.h2.spreadful;

/**
 * 
 * @author Jason
 */
public class Greeting {
    private final long id;
    private final String content;
    
    public Greeting(long l, String content) {
    	this.id = l;
    	this.content = content;
    }

	public String getContent() {
		return content;
	}

	public long getId() {
		return id;
	}
}
