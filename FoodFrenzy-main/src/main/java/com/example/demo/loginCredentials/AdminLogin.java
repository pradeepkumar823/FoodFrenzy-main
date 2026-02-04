package com.example.demo.loginCredentials;

public class AdminLogin
{
	
	private String adminEmail;
	
	private String adminPassword;
	
	public String getAdminEmail() {
		return adminEmail;
	}
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	@Override
	public String toString() {
		return "AdminLogin [name=" + adminEmail + ", password=" + adminPassword + "]";
	}	
}