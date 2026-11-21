package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ApiResponse;
import com.example.entity.User;
import com.example.service.UserServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/users", "/users"})
@RequiredArgsConstructor
public class UserController {
	private final UserServices userService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
		return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", userService.getAllUsers()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", userService.getUserById(id)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {
		User created = userService.createUser(user);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new ApiResponse<>(true, "User created successfully", created));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
		return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", userService.updateUser(id, user)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
	}
}
