package com.app.fooddelivery.auth.controller;

import com.app.fooddelivery.auth.dto.LoginRequest;
import com.app.fooddelivery.auth.dto.RegisterRequest;
import com.app.fooddelivery.auth.service.AuthService;
import com.app.fooddelivery.security.JwtService;
import com.app.fooddelivery.user.entity.User;
import com.app.fooddelivery.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

     // регистрация и логин
    @PostMapping("/register")
    public String register(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    // получение текущего пользователя
    // в лоб
//    @GetMapping("/me")
//    public String me() {
//        return SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getName();
//    }

    // чуть лучше.

    @GetMapping("/me")
    public Map<String, Object> me(){
        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return Map.of(
                "username", auth.getName(),
                "roles", auth.getAuthorities()
        );
    }





}
