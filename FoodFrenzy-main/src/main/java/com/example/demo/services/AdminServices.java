package com.example.demo.services;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;

import jakarta.transaction.Transactional;

@Component
@Service
@Transactional
public class AdminServices {
	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Admin> getAll() {
		return (List<Admin>) this.adminRepository.findAll();
	}

	public Admin getAdmin(int adminId) {
		return this.adminRepository.findByAdminId(adminId)
				.orElse(null);
	}

	public void update(Admin admin, int adminId) {
		admin.setAdminId(adminId);
		if (admin.getAdminPassword() != null && !admin.getAdminPassword().startsWith("$2a$")) {
			admin.setAdminPassword(passwordEncoder.encode(admin.getAdminPassword()));
		}
		this.adminRepository.save(admin);
	}

	public void delete(int adminId) {
		this.adminRepository.deleteById(adminId);
	}

	// Normalize email to handle Gmail's dot-ignoring behavior
	private String normalizeEmail(String email) {
		if (email == null) {
			return null;
		}
		email = email.toLowerCase().trim();

		// For Gmail addresses, remove dots before @ symbol
		if (email.endsWith("@gmail.com")) {
			String[] parts = email.split("@");
			if (parts.length == 2) {
				String localPart = parts[0].replace(".", "");
				email = localPart + "@gmail.com";
			}
		}
		return email;
	}

	public void addAdmin(Admin admin) {
		String encodePassword = passwordEncoder.encode(admin.getAdminPassword());
		admin.setAdminPassword(encodePassword);

		// Normalize email (handles Gmail dots and lowercase)
		if (admin.getAdminEmail() != null) {
			admin.setAdminEmail(normalizeEmail(admin.getAdminEmail()));
		}

		// Generate verification code
		String verificationCode = UUID.randomUUID().toString();
		admin.setVerificationCode(verificationCode);
		admin.setVerified(false); // Requires Google OAuth verification

		this.adminRepository.save(admin);
	}

	public boolean isAdminVerified(String email) {
		if (email == null)
			return false;
		return adminRepository.findByAdminEmailIgnoreCase(normalizeEmail(email))
				.map(Admin::isVerified)
				.orElse(false);
	}

	public boolean validateAdminCredentials(String email, String password) {
		if (email == null)
			return false;
		Admin admin = adminRepository.findByAdminEmailIgnoreCase(normalizeEmail(email)).orElse(null);
		if (admin != null) {
			return passwordEncoder.matches(password, admin.getAdminPassword());
		}
		return false;
	}

	public boolean validateAdminEmail(String email) {
		if (email == null)
			return false;
		return this.adminRepository.findByAdminEmailIgnoreCase(normalizeEmail(email)).isPresent();
	}

	public boolean verifyAdmin(String code) {
		return getAll().stream()
				.filter(a -> code.equals(a.getVerificationCode()))
				.findFirst()
				.map(admin -> {
					if (!admin.isVerified()) {
						admin.setVerified(true);
						admin.setVerificationCode(null);
						adminRepository.save(admin);
						return true;
					}
					return false;
				})
				.orElse(false);
	}
}