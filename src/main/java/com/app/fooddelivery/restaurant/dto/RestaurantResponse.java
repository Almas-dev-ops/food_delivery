package com.app.fooddelivery.restaurant.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantResponse {
    private Long id;
    private  String name;
    private  String description;
}
