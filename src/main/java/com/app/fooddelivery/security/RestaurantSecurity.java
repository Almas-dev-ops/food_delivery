package com.app.fooddelivery.security;

import com.app.fooddelivery.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantSecurity {
    private final RestaurantRepository repository;

    public boolean isOwner(Long restaurantId, String username){
        return repository.findById(restaurantId)
                .map(r->r.getOwner().getUsername().equals(username))
                .orElse(false);
    }
}
