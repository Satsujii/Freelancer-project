package com.freelance.marketplace.controller;

import com.freelance.marketplace.dto.FreelancerProfileDto;
import com.freelance.marketplace.entity.FreelancerProfile;
import com.freelance.marketplace.security.CustomUserPrincipal;
import com.freelance.marketplace.service.FreelancerProfileService;
import com.freelance.marketplace.service.JobApplicationService;
import com.freelance.marketplace.dto.JobApplicationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/freelancer")
@PreAuthorize("hasRole('FREELANCER')")
public class FreelancerController {
    @Autowired
    private FreelancerProfileService freelancerService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @GetMapping("/profile")
    public ResponseEntity<FreelancerProfileDto> getProfile(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
        FreelancerProfile profile = freelancerService.getProfile(userId);
        FreelancerProfileDto dto = new FreelancerProfileDto();
        dto.setBio(profile.getBio());
        dto.setSkills(profile.getSkills());
        dto.setWebsite(profile.getWebsite());
        dto.setCreatedAt(profile.getCreatedAt());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/applications")
    public ResponseEntity<List<JobApplicationDto>> getMyApplications(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
        List<JobApplicationDto> applications = jobApplicationService.getApplicationsForFreelancer(userId);
        return ResponseEntity.ok(applications);
    }

    @PutMapping("/profile")
    public ResponseEntity<FreelancerProfileDto> updateProfile(@RequestBody FreelancerProfile data, Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
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
        Long userId = principal.getId();
        freelancerService.deleteProfile(userId);
        return ResponseEntity.noContent().build();
    }
} 