package com.DingDong.Ding.Dong.Tracker.service;

import com.DingDong.Ding.Dong.Tracker.dto.request.ChangePasswordRequest;
import com.DingDong.Ding.Dong.Tracker.dto.request.UpdateProfileRequest;
import com.DingDong.Ding.Dong.Tracker.dto.response.UserResponse;
import com.DingDong.Ding.Dong.Tracker.entity.User;
import com.DingDong.Ding.Dong.Tracker.enums.Gender;
import com.DingDong.Ding.Dong.Tracker.enums.PreferredUnits;
import com.DingDong.Ding.Dong.Tracker.exception.BadRequestException;
import com.DingDong.Ding.Dong.Tracker.exception.ResourceNotFoundException;
import com.DingDong.Ding.Dong.Tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getProfile(Long userId) {
        User user = findUserOrThrow(userId);
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = findUserOrThrow(userId);

        if (StringUtils.hasText(request.firstName())) user.setFirstName(request.firstName());
        if (StringUtils.hasText(request.lastName())) user.setLastName(request.lastName());
        if (StringUtils.hasText(request.timezone())) user.setTimezone(request.timezone());

        if (StringUtils.hasText(request.gender())) {
            try {
                user.setGender(Gender.valueOf(request.gender().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid gender value: " + request.gender());
            }
        }

        if (StringUtils.hasText(request.preferredUnits())) {
            try {
                user.setPreferredUnits(PreferredUnits.valueOf(request.preferredUnits().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid preferred units value: " + request.preferredUnits());
            }
        }

        user = userRepository.save(user);
        return UserResponse.from(user);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = findUserOrThrow(userId);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(Long userId) {
        User user = findUserOrThrow(userId);
        user.setDeletedAt(LocalDateTime.now());
        user.setIsActive(false);
        userRepository.save(user);
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
