package com.DingDong.Ding.Dong.Tracker.service;

import com.DingDong.Ding.Dong.Tracker.dto.request.LoginRequest;
import com.DingDong.Ding.Dong.Tracker.dto.request.RegisterRequest;
import com.DingDong.Ding.Dong.Tracker.dto.response.AuthResponse;
import com.DingDong.Ding.Dong.Tracker.entity.User;
import com.DingDong.Ding.Dong.Tracker.exception.BadRequestException;
import com.DingDong.Ding.Dong.Tracker.exception.ConflictException;
import com.DingDong.Ding.Dong.Tracker.repository.UserRepository;
import com.DingDong.Ding.Dong.Tracker.security.JwtTokenProvider;
import com.DingDong.Ding.Dong.Tracker.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(request.email())) {
            throw new ConflictException("Email is already registered");
        }
        if (userRepository.existsByUsernameAndDeletedAtIsNull(request.username())) {
            throw new ConflictException("Username is already taken");
        }
        if (request.dateOfBirth() != null) {
            int age = Period.between(request.dateOfBirth(), java.time.LocalDate.now()).getYears();
            if (age < 13) {
                throw new BadRequestException("You must be at least 13 years old to register");
            }
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setDateOfBirth(request.dateOfBirth());
        user = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getId());
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        userRepository.findByIdAndDeletedAtIsNull(principal.getId()).ifPresent(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
        });

        User user = userRepository.findByIdAndDeletedAtIsNull(principal.getId()).orElseThrow();
        String token = jwtTokenProvider.generateToken(user.getId());
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
    }
}
