package com.app.fooddelivery.restaurant.repository;

import com.app.fooddelivery.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
