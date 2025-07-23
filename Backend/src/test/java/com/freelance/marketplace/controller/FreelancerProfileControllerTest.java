package com.freelance.marketplace.controller;

import com.freelance.marketplace.service.FreelancerProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;
import com.freelance.marketplace.security.CustomUserPrincipal;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.entity.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import com.freelance.marketplace.dto.FreelancerProfileResponse;
import com.freelance.marketplace.dto.FreelancerProfileRequest;

@WithMockUser(username = "1", roles = {"FREELANCER"})
@SpringBootTest
@AutoConfigureMockMvc
class FreelancerProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FreelancerProfileService freelancerProfileService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        public FreelancerProfileService freelancerProfileService() {
            return org.mockito.Mockito.mock(FreelancerProfileService.class);
        }
    }

    @Test
    @DisplayName("POST /api/freelancer-profiles returns 200")
    void createProfile() throws Exception {
        when(freelancerProfileService.createProfile(anyLong(), any(FreelancerProfileRequest.class))).thenReturn(new FreelancerProfileResponse());
        User user = new User(4L, "Freelancer", "freelancer@freelance.com", "freelancer123456", Role.FREELANCER, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        mockMvc.perform(post("/api/freelancer-profiles")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/freelancer-profiles returns 200")
    void updateProfile() throws Exception {
        when(freelancerProfileService.updateProfile(anyLong(), any(com.freelance.marketplace.dto.FreelancerProfileRequest.class))).thenReturn(null);
        mockMvc.perform(put("/api/freelancer-profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/freelancer-profiles/me returns 200")
    void getMyProfile() throws Exception {
        when(freelancerProfileService.getProfileByUserId(anyLong())).thenReturn(null);
        mockMvc.perform(get("/api/freelancer-profiles/me"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/freelancer-profiles/{userId} returns 200")
    void getProfileByUserId() throws Exception {
        when(freelancerProfileService.getProfileByUserId(anyLong())).thenReturn(null);
        mockMvc.perform(get("/api/freelancer-profiles/1"))
                .andExpect(status().isOk());
    }
} 