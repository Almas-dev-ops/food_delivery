package com.app.fooddelivery.auth.service;

import com.app.fooddelivery.auth.dto.LoginRequest;
import com.app.fooddelivery.auth.dto.RegisterRequest;
import com.app.fooddelivery.security.JwtService;
import com.app.fooddelivery.user.entity.User;
import com.app.fooddelivery.user.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    //Регистрация
    public String register(RegisterRequest request) {

        // проверка: существует пользователь?
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        return "User registered";
    }

     //    логин
    public String login(LoginRequest request){

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("invalid password");
        }

        return jwtService.generateToken(user.getUsername());
    }


}
