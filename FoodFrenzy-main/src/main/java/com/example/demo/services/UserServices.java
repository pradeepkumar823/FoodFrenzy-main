package com.example.demo.services;

import java.util.*;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;

@Service
public class UserServices {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> getAllUser() {
		return (List<User>) this.userRepository.findAll();
	}

	public User getUser(int userId) {
		return this.userRepository.findById(userId)
				.orElse(null);

	}

	public User getUserById(int userId) {
		return this.userRepository.findByUserId(userId)
				.orElse(null);
	}

	public User getUserByEmail(String email) {
		return this.userRepository.findByUserEmail(email).orElse(null);
	}

	public void updateUser(User user, int userId) {
		user.setUserId(userId);
		// encode password before saving if its being updated
		if (user.getUserPassword() != null && !user.getUserPassword().startsWith("$2a$")) {
			user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
		}
		this.userRepository.save(user);
	}

	public void deleteUser(int id) {
		this.userRepository.deleteById(id);
	}

	public void addUser(User user) {
		// encode password before saving
		String encodedPassword = passwordEncoder.encode(user.getUserPassword());
		user.setUserPassword(encodedPassword);

		// Generate verification code
		String verificationCode = UUID.randomUUID().toString();
		user.setVerificationCode(verificationCode);
		user.setVerified(false);

		this.userRepository.save(user);
	}

	public boolean validateLoginCredentials(String email, String password) {
		User user = this.userRepository.findByUserEmail(email).orElse(null);
		if (user != null) {
			if (!user.isVerified()) {
				return false; // Not verified
			}
			// Use passwordEncoder.matches() for secure comparison
			return passwordEncoder.matches(password, user.getUserPassword());
		}
		return false;
	}

	public boolean verifyUser(String code) {
		return getAllUser().stream()
				.filter(u -> code.equals(u.getVerificationCode()))
				.findFirst()
				.map(user -> {
					if (!user.isVerified()) {
						user.setVerified(true);
						user.setVerificationCode(null);
						userRepository.save(user);
						return true;
					}
					return false;
				})
				.orElse(false);
	}

	public boolean validationLoginCredentials(String email) {
		User user = this.userRepository.findByUserEmail(email).orElse(null);
		if (user != null) {
			return true;
		}
		return false;
	}

}