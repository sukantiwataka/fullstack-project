package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.Restaurant;
import com.example.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantService {
	private final RestaurantRepository restaurantRepository;

	public List<Restaurant> getAllRestaurants() {
		return restaurantRepository.findAll();
	}

	public Restaurant getRestaurantById(Long id) {
		return restaurantRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Restaurant not found: " + id));
	}

	public Restaurant createRestaurant(Restaurant restaurant) {
		restaurant.setId(null);
		return restaurantRepository.save(restaurant);
	}

	public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
		Restaurant existing = getRestaurantById(id);
		existing.setName(restaurant.getName());
		existing.setCuisine(restaurant.getCuisine());
		existing.setAddress(restaurant.getAddress());
		existing.setPhone(restaurant.getPhone());
		return restaurantRepository.save(existing);
	}

	public void deleteRestaurant(Long id) {
		restaurantRepository.delete(getRestaurantById(id));
	}
}
