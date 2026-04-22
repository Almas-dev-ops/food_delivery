package com.app.fooddelivery.auth.controller;

import com.app.fooddelivery.auth.dto.LoginRequest;
import com.app.fooddelivery.auth.dto.RegisterRequest;
import com.app.fooddelivery.auth.service.AuthService;
import com.app.fooddelivery.security.JwtService;
import com.app.fooddelivery.user.entity.User;
import com.app.fooddelivery.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public String register(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }


}
