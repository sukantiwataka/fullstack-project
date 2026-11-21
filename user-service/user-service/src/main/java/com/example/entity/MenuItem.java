package com.example.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Restaurant id is required")
	@Positive(message = "Restaurant id must be positive")
	private Long restaurantId;

	@NotBlank(message = "Menu item name is required")
	private String name;

	private String description;
	private String category;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.01", message = "Price must be greater than zero")
	private BigDecimal price;

	private boolean available;
}
