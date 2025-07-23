package com.freelance.marketplace.controller;

import com.freelance.marketplace.service.FreelancerProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FreelancerProfileController.class)
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
        when(freelancerProfileService.createProfile(anyLong(), any(com.freelance.marketplace.dto.FreelancerProfileRequest.class))).thenReturn(null);
        mockMvc.perform(post("/api/freelancer-profiles")
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