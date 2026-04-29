package com.app.fooddelivery.auth.service;

import com.app.fooddelivery.auth.dto.AuthResponse;
import com.app.fooddelivery.auth.dto.LoginRequest;
import com.app.fooddelivery.auth.dto.RegisterRequest;
import com.app.fooddelivery.auth.entity.RefreshToken;
import com.app.fooddelivery.auth.repository.RefreshTokenRepository;
import com.app.fooddelivery.security.JwtService;
import com.app.fooddelivery.user.entity.Role;
import com.app.fooddelivery.user.entity.User;
import com.app.fooddelivery.user.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    //Регистрация
    public String register(RegisterRequest request) {

        // проверка: существует пользователь?
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();


        userRepository.save(user);
        return "User registered";
    }

//     //    логин
//    public String login(LoginRequest request){
//
//        User user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
//            throw new RuntimeException("invalid password");
//        }
//
//        return jwtService.generateToken(user.getUsername(),user.getRole().name());
//    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtService.generateAccessToken(
                user.getUsername(),
                user.getRole().name()
        );

        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        refreshTokenRepository.deleteByUser(user);

        RefreshToken tokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(tokenEntity);
        return new AuthResponse(accessToken, refreshToken);
    }

    public String refresh(String refreshToken){

        System.out.println("Request token: [" + refreshToken + "]");
        refreshTokenRepository.findAll().forEach(t-> System.out.println("DB token: [" + t.getToken()+"]" ));

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(()-> new RuntimeException("Invalid refresh token"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Refresh token expired");
        }

        User user = token.getUser();
        return jwtService.generateAccessToken(
                user.getUsername(),
                user.getRole().name()
        );
    }

    public void logout(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        refreshTokenRepository.delete(token);

    }


}
