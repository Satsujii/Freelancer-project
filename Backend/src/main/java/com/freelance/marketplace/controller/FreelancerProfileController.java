package com.freelance.marketplace.controller;

import com.freelance.marketplace.dto.FreelancerProfileRequest;
import com.freelance.marketplace.dto.FreelancerProfileResponse;
import com.freelance.marketplace.service.FreelancerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/freelancer-profiles")
@RequiredArgsConstructor
public class FreelancerProfileController {
    private final FreelancerProfileService freelancerProfileService;

    // Create profile (one per user)
    @PostMapping
    public ResponseEntity<FreelancerProfileResponse> createProfile(@RequestBody FreelancerProfileRequest request) {
        Long userId = getCurrentUserId();
        FreelancerProfileResponse response = freelancerProfileService.createProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    // Update profile
    @PutMapping
    public ResponseEntity<FreelancerProfileResponse> updateProfile(@RequestBody FreelancerProfileRequest request) {
        Long userId = getCurrentUserId();
        FreelancerProfileResponse response = freelancerProfileService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    // Get current user's profile
    @GetMapping("/me")
    public ResponseEntity<FreelancerProfileResponse> getMyProfile() {
        Long userId = getCurrentUserId();
        FreelancerProfileResponse response = freelancerProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // Get profile by userId (for public viewing)
    @GetMapping("/{userId}")
    public ResponseEntity<FreelancerProfileResponse> getProfileByUserId(@PathVariable Long userId) {
        FreelancerProfileResponse response = freelancerProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // Helper method to extract current user ID from security context
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Adjust this based on how you store user details in your project
        // For example, if using a custom UserDetails with getId(), cast and use that
        // Or if the username is the user ID, parse it to Long
        return Long.parseLong(authentication.getName());
    }
}
