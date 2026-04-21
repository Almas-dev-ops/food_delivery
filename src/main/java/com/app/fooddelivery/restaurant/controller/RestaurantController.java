package com.app.fooddelivery.restaurant.controller;

import com.app.fooddelivery.restaurant.dto.CreateRestaurantRequest;
import com.app.fooddelivery.restaurant.dto.RestaurantResponse;
import com.app.fooddelivery.restaurant.entity.Restaurant;
import com.app.fooddelivery.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private  final RestaurantService service;
//CRUD
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse create(@RequestBody
                                         @Valid CreateRestaurantRequest request){
        return service.create(request);
    }

    @GetMapping
    public Page<RestaurantResponse> getAll(Pageable pageable){
        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Restaurant getById(
            @PathVariable Long id){
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public RestaurantResponse update(
            @PathVariable Long id,
            @RequestBody @Valid CreateRestaurantRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

//    Pagination
    @GetMapping("/search")
    public Page<RestaurantResponse> search(
            @RequestParam String name,
            Pageable pageable){
        return service.search(name, pageable);
    }

}
