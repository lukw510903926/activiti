package com.activiti.core.common.utils;

import java.io.Serializable;

public class LoginUser implements Serializable{

	private static final long serialVersionUID = 1L;

	private String username;
	
	private String name;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
