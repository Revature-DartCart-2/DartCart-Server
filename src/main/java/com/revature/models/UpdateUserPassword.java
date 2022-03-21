package com.revature.models;

public class UpdateUserPassword {
	
	public UpdateUserPassword() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UpdateUserPassword(String password) {
		super();
		this.password = password;
	}

}
