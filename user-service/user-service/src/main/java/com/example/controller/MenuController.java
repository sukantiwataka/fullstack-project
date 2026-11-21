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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ApiResponse;
import com.example.entity.MenuItem;
import com.example.service.MenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/menu-items", "/menu-items"})
@RequiredArgsConstructor
public class MenuController {
	private final MenuService menuService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<MenuItem>>> getAllMenuItems(
			@RequestParam(required = false) Long restaurantId) {
		List<MenuItem> items = restaurantId == null
			? menuService.getAllMenuItems()
			: menuService.getMenuItemsByRestaurant(restaurantId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Menu items retrieved successfully", items));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<MenuItem>> getMenuItemById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Menu item retrieved successfully", menuService.getMenuItemById(id)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<MenuItem>> createMenuItem(@Valid @RequestBody MenuItem menuItem) {
		MenuItem created = menuService.createMenuItem(menuItem);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new ApiResponse<>(true, "Menu item created successfully", created));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<MenuItem>> updateMenuItem(@PathVariable Long id, @Valid @RequestBody MenuItem menuItem) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Menu item updated successfully", menuService.updateMenuItem(id, menuItem)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteMenuItem(@PathVariable Long id) {
		menuService.deleteMenuItem(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Menu item deleted successfully", null));
	}
}
