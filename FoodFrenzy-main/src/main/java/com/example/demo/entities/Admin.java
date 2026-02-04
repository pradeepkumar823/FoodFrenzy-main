package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "admin_table")
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int adminId;

	@NotNull
	private String adminName;

	@NotNull
	@Email
	@Column(unique = true)
	private String adminEmail;

	@NotNull
	private String adminPassword;

	@NotNull
	@Column(unique = true)
	private String adminPhoneNumber;

	private String verificationCode;
	private boolean verified = false;

	public Admin() {
	}

	public Admin(String adminName, String adminEmail, String adminPassword, String adminPhoneNumber) {
		this.adminName = adminName;
		this.adminEmail = adminEmail;
		this.adminPassword = adminPassword;
		this.adminPhoneNumber = adminPhoneNumber;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

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

	public String getAdminPhoneNumber() {
		return adminPhoneNumber;
	}

	public void setAdminPhoneNumber(String adminPhoneNumber) {
		this.adminPhoneNumber = adminPhoneNumber;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Admin admin = (Admin) o;
		return adminId == admin.adminId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(adminId);
	}

	@Override
	public String toString() {
		return "Admin [adminId=" + adminId + ", adminName=" + adminName + ", adminEmail=" + adminEmail
				+ ", adminPassword="
				+ adminPassword + ", adminPhoneNumber=" + adminPhoneNumber + "]";
	}
}