package com.freelance.marketplace.controller;

import com.freelance.marketplace.entity.ClientProfile;
import com.freelance.marketplace.security.CustomUserPrincipal;
import com.freelance.marketplace.service.ClientProfileService;
import com.freelance.marketplace.dto.ClientProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@PreAuthorize("hasRole('CLIENT')")
public class ClientController {
    @Autowired
    private ClientProfileService clientService;

    @GetMapping("/profile")
    public ResponseEntity<ClientProfileDto> getProfile(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getUser().getId();
        ClientProfile profile = clientService.getProfile(userId);
        ClientProfileDto dto = new ClientProfileDto();
        dto.setCompanyName(profile.getCompanyName());
        dto.setBio(profile.getBio());
        dto.setWebsite(profile.getWebsite());
        dto.setCreatedAt(profile.getCreatedAt());
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/profile")
    public ResponseEntity<ClientProfileDto> updateProfile(@RequestBody ClientProfile data, Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getUser().getId();
        ClientProfile updated = clientService.updateProfile(userId, data);
        ClientProfileDto dto = new ClientProfileDto();
        dto.setCompanyName(updated.getCompanyName());
        dto.setBio(updated.getBio());
        dto.setWebsite(updated.getWebsite());
        dto.setCreatedAt(updated.getCreatedAt());
        return ResponseEntity.ok(dto);
    }


}
