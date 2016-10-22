package com.flores.h2.spreadbaseio.model.impl;

/**
 * Simple version pojo for start
 * up testing...
 * @author Jason
 */
public class Version {
	private String name;
	private String version;

	public Version() {}
	public Version(String name, String version) {
		this.name = name;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String toString() {
		return String.format("{name:%s version:%s}", name, version);
	}
}