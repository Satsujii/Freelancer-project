package com.freelance.marketplace.controller;

import com.freelance.marketplace.service.FreelancerProfileService;
import com.freelance.marketplace.service.JobApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collections;

@WebMvcTest(FreelancerController.class)
class FreelancerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FreelancerProfileService freelancerService;
    @Autowired
    private JobApplicationService jobApplicationService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        public FreelancerProfileService freelancerProfileService() {
            return org.mockito.Mockito.mock(FreelancerProfileService.class);
        }
        @Bean
        @Primary
        public JobApplicationService jobApplicationService() {
            return org.mockito.Mockito.mock(JobApplicationService.class);
        }
    }

    @Test
    @WithMockUser(roles = "FREELANCER")
    @DisplayName("GET /api/freelancer/profile returns 200")
    void getProfile() throws Exception {
        when(freelancerService.getProfile(anyLong())).thenReturn(null);
        mockMvc.perform(get("/api/freelancer/profile"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "FREELANCER")
    @DisplayName("GET /api/freelancer/applications returns 200")
    void getMyApplications() throws Exception {
        when(jobApplicationService.getApplicationsForFreelancer(anyLong())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/freelancer/applications"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "FREELANCER")
    @DisplayName("PUT /api/freelancer/profile returns 200")
    void updateProfile() throws Exception {
        when(freelancerService.updateProfile(anyLong(), any(com.freelance.marketplace.entity.FreelancerProfile.class))).thenReturn(null);
        mockMvc.perform(put("/api/freelancer/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "FREELANCER")
    @DisplayName("DELETE /api/freelancer/profile returns 204")
    void deleteProfile() throws Exception {
        doNothing().when(freelancerService).deleteProfile(anyLong());
        mockMvc.perform(delete("/api/freelancer/profile"))
                .andExpect(status().isNoContent());
    }
} 