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
import com.example.entity.Restaurant;
import com.example.service.RestaurantService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/restaurants", "/restaurants"})
@RequiredArgsConstructor
public class RestaurantController {
	private final RestaurantService restaurantService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<Restaurant>>> getAllRestaurants() {
		return ResponseEntity.ok(new ApiResponse<>(true, "Restaurants retrieved successfully", restaurantService.getAllRestaurants()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Restaurant>> getRestaurantById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Restaurant retrieved successfully", restaurantService.getRestaurantById(id)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Restaurant>> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
		Restaurant created = restaurantService.createRestaurant(restaurant);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new ApiResponse<>(true, "Restaurant created successfully", created));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Restaurant>> updateRestaurant(@PathVariable Long id, @Valid @RequestBody Restaurant restaurant) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Restaurant updated successfully", restaurantService.updateRestaurant(id, restaurant)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteRestaurant(@PathVariable Long id) {
		restaurantService.deleteRestaurant(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Restaurant deleted successfully", null));
	}
}
