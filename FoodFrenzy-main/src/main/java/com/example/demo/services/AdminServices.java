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

	public void addAdmin(Admin admin) {
		String encodePassword = passwordEncoder.encode(admin.getAdminPassword());
		admin.setAdminPassword(encodePassword);

		// Always store email in lowercase
		if (admin.getAdminEmail() != null) {
			admin.setAdminEmail(admin.getAdminEmail().toLowerCase());
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
		return adminRepository.findByAdminEmailIgnoreCase(email.toLowerCase())
				.map(Admin::isVerified)
				.orElse(false);
	}

	public boolean validateAdminCredentials(String email, String password) {
		if (email == null)
			return false;
		Admin admin = adminRepository.findByAdminEmailIgnoreCase(email.toLowerCase()).orElse(null);
		if (admin != null) {
			return passwordEncoder.matches(password, admin.getAdminPassword());
		}
		return false;
	}

	public boolean validateAdminEmail(String email) {
		if (email == null)
			return false;
		return this.adminRepository.findByAdminEmailIgnoreCase(email.toLowerCase()).isPresent();
	}

	public Admin createAdminFromOAuth(String email, String name) {
		Admin admin = new Admin();
		admin.setAdminEmail(email.toLowerCase());
		admin.setAdminName(name != null ? name : "Admin");
		admin.setAdminPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // Random password
		admin.setAdminPhoneNumber("OAuth-" + UUID.randomUUID().toString().substring(0, 8)); // Placeholder
		admin.setVerified(true);
		return adminRepository.save(admin);
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