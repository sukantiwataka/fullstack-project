package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.entity.MenuItem;
import com.example.entity.Restaurant;
import com.example.entity.User;
import com.example.repository.MenuItemRepository;
import com.example.repository.RestaurantRepository;
import com.example.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner seedData(RestaurantRepository restaurants, MenuItemRepository menuItems, UserRepository users) {
		return args -> {
			if (restaurants.count() > 0) return;

			Restaurant italy = restaurants.save(Restaurant.builder()
				.name("Casa Italia").cuisine("Italian").address("18 Market Street").phone("+1 555 0102").build());
			Restaurant garden = restaurants.save(Restaurant.builder()
				.name("Green Garden").cuisine("Mediterranean").address("42 Park Avenue").phone("+1 555 0144").build());

			menuItems.saveAll(List.of(
				MenuItem.builder().restaurantId(italy.getId()).name("Truffle Mushroom Pasta")
					.description("Creamy pasta with wild mushrooms").category("Main course")
					.price(new BigDecimal("18.50")).available(true).build(),
				MenuItem.builder().restaurantId(italy.getId()).name("Tomato Burrata")
					.description("Heirloom tomatoes and fresh burrata").category("Starter")
					.price(new BigDecimal("12.00")).available(true).build(),
				MenuItem.builder().restaurantId(garden.getId()).name("Garden Grain Bowl")
					.description("Seasonal vegetables, quinoa and herbs").category("Main course")
					.price(new BigDecimal("14.25")).available(true).build()
			));

			users.saveAll(List.of(
				User.builder().name("Avery Morgan").email("avery@example.com").phone("+1 555 0188").role("Admin").build(),
				User.builder().name("Sam Rivera").email("sam@example.com").phone("+1 555 0117").role("Customer").build()
			));
		};
	}
}
