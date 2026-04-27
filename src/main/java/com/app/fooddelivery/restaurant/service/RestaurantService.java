package com.app.fooddelivery.restaurant.service;

import com.app.fooddelivery.common.exception.NotFoundException;
import com.app.fooddelivery.restaurant.dto.CreateRestaurantRequest;
import com.app.fooddelivery.restaurant.dto.RestaurantResponse;
import com.app.fooddelivery.restaurant.entity.Restaurant;
import com.app.fooddelivery.restaurant.repository.RestaurantRepository;
import com.app.fooddelivery.user.entity.User;
import com.app.fooddelivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository repository;
    private final UserRepository userRepository;
 //CRUD
    public Page<RestaurantResponse> getAll(Pageable pageable){

        String username = getCurrentUsername();
        Page<Restaurant> page;

        if (isAdmin()){
            page = repository.findAll(pageable);
        }else {
            page = repository.findByOwnerUsername(username, pageable);
        }


        return page.map(restaurant -> mapToResponse(restaurant));
    }

    public  Restaurant getById( Long id){
        return repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Restaurant not found"));
    }

    public RestaurantResponse create(CreateRestaurantRequest request){

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("user not found"));

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(user) // владелец
                .createdAt(LocalDateTime.now())
                .build();
        Restaurant saved = repository.save(restaurant);
        return mapToResponse(saved);
    }

    public RestaurantResponse update(Long id, CreateRestaurantRequest request){
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Restaurant not found"));

        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        return mapToResponse(repository.save(restaurant));
    }

    public void delete(Long id){
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Restaurant not found"));

        String username = getCurrentUsername();
        boolean isOwner = restaurant.getOwner().getUsername().equals(username);

        if (!isOwner && !isAdmin()){
            throw new RuntimeException("Access denied");
        }
        repository.delete(restaurant);

    }

// Mapper
    public RestaurantResponse mapToResponse(Restaurant restaurant){
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .build();
    }

// Pageable
    public Page<RestaurantResponse> search(String name, Pageable pageable){
        return repository.findByNameContainingIgnoreCase(name, pageable)
                .map(restaurant -> mapToResponse(restaurant));
    }

    private String getCurrentUsername(){
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    public Page<RestaurantResponse> getMyRestaurants(Pageable pageable){
        String username = getCurrentUsername();

        return repository.findByOwnerUsername(username,pageable)
                .map(restaurant -> mapToResponse(restaurant));

    }

    private boolean isAdmin() {
    return SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
    }






}
