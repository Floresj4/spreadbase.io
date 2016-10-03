package com.flores.h2.spreadful.greeting;


/**
 * 
 * @author Jason
 */
public class Greeting {

	private long id;
	private String content;
	
    public String getContent() {
		return content;
	}

	public long getId() {
		return id;
	}

	public void setContent(String content) {
    	this.content = content;
    }

	public void setId(long id) {
		this.id = id;
	}
}