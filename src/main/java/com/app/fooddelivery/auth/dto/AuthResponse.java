package com.app.fooddelivery.auth.dto;

public record AuthResponse(
        String accessToken,
        String  refreshToken) {
}
