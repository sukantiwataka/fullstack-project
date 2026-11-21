package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.MenuItem;
import com.example.repository.MenuItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {
	private final MenuItemRepository menuItemRepository;

	public List<MenuItem> getAllMenuItems() {
		return menuItemRepository.findAll();
	}

	public List<MenuItem> getMenuItemsByRestaurant(Long restaurantId) {
		return menuItemRepository.findByRestaurantId(restaurantId);
	}

	public MenuItem getMenuItemById(Long id) {
		return menuItemRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Menu item not found: " + id));
	}

	public MenuItem createMenuItem(MenuItem menuItem) {
		menuItem.setId(null);
		return menuItemRepository.save(menuItem);
	}

	public MenuItem updateMenuItem(Long id, MenuItem menuItem) {
		MenuItem existing = getMenuItemById(id);
		existing.setRestaurantId(menuItem.getRestaurantId());
		existing.setName(menuItem.getName());
		existing.setDescription(menuItem.getDescription());
		existing.setCategory(menuItem.getCategory());
		existing.setPrice(menuItem.getPrice());
		existing.setAvailable(menuItem.isAvailable());
		return menuItemRepository.save(existing);
	}

	public void deleteMenuItem(Long id) {
		menuItemRepository.delete(getMenuItemById(id));
	}
}
