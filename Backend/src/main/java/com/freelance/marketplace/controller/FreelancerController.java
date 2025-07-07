package com.freelance.marketplace.controller;

import com.freelance.marketplace.dto.FreelancerProfileDto;
import com.freelance.marketplace.entity.FreelancerProfile;
import com.freelance.marketplace.security.CustomUserPrincipal;
import com.freelance.marketplace.service.FreelancerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/freelancer")
@PreAuthorize("hasRole('FREELANCER')")
public class FreelancerController {
    @Autowired
    private FreelancerProfileService freelancerService;

    @GetMapping("/profile")
    public ResponseEntity<FreelancerProfileDto> getProfile(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getUser().getId();
        FreelancerProfile profile = freelancerService.getProfile(userId);
        FreelancerProfileDto dto = new FreelancerProfileDto();
        dto.setBio(profile.getBio());
        dto.setSkills(profile.getSkills());
        dto.setWebsite(profile.getWebsite());
        dto.setCreatedAt(profile.getCreatedAt());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/profile")
    public ResponseEntity<FreelancerProfileDto> updateProfile(@RequestBody FreelancerProfile data, Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getUser().getId();
        FreelancerProfile updated = freelancerService.updateProfile(userId, data);
        FreelancerProfileDto dto = new FreelancerProfileDto();
        dto.setBio(updated.getBio());
        dto.setSkills(updated.getSkills());
        dto.setWebsite(updated.getWebsite());
        dto.setCreatedAt(updated.getCreatedAt());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteProfile(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getUser().getId();
        freelancerService.deleteProfile(userId);
        return ResponseEntity.noContent().build();
    }
} 