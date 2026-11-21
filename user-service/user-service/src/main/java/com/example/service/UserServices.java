package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServices {
	private final UserRepository userRepository;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("User not found: " + id));
	}

	public User createUser(User user) {
		user.setId(null);
		return userRepository.save(user);
	}

	public User updateUser(Long id, User user) {
		User existing = getUserById(id);
		existing.setName(user.getName());
		existing.setEmail(user.getEmail());
		existing.setPhone(user.getPhone());
		existing.setRole(user.getRole());
		return userRepository.save(existing);
	}

	public void deleteUser(Long id) {
		userRepository.delete(getUserById(id));
	}
}
