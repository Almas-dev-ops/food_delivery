package com.app.fooddelivery.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRestaurantRequest {
    private String name;
    private String description;

}
