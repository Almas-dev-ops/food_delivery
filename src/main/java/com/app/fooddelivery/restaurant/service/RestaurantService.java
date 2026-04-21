package com.app.fooddelivery.restaurant.service;

import com.app.fooddelivery.common.exception.NotFoundException;
import com.app.fooddelivery.restaurant.dto.CreateRestaurantRequest;
import com.app.fooddelivery.restaurant.dto.RestaurantResponse;
import com.app.fooddelivery.restaurant.entity.Restaurant;
import com.app.fooddelivery.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository repository;

    public List<RestaurantResponse> getAll(){
        return repository.findAll()
                .stream()
                .map(restaurant -> mapToResponse(restaurant))
                .toList();
    }

    public  Restaurant getById( Long id){
        return repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Restaurant not found"));
    }

    public RestaurantResponse mapToResponse(Restaurant restaurant){
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .build();
    }

    public RestaurantResponse create(CreateRestaurantRequest request){
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
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
        repository.delete(restaurant);

    }


}
