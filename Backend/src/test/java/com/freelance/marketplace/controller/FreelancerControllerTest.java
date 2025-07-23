package com.freelance.marketplace.controller;

import com.freelance.marketplace.service.FreelancerProfileService;
import com.freelance.marketplace.service.JobApplicationService;
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

import java.util.Arrays;
import java.util.Collections;
import com.freelance.marketplace.security.CustomUserPrincipal;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.entity.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import com.freelance.marketplace.entity.FreelancerProfile;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
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
    @DisplayName("GET /api/freelancer/profile returns 200")
    void getProfile() throws Exception {
        when(freelancerService.getProfile(anyLong())).thenReturn(new FreelancerProfile());
        User user = new User(3L, "Freelancer", "freelancer@freelance.com", "freelancer123456", Role.FREELANCER, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        mockMvc.perform(get("/api/freelancer/profile")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/freelancer/applications returns 200")
    void getMyApplications() throws Exception {
        when(jobApplicationService.getApplicationsForFreelancer(anyLong())).thenReturn(Collections.emptyList());
        User user = new User(3L, "Freelancer", "freelancer@freelance.com", "freelancer123456", Role.FREELANCER, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        mockMvc.perform(get("/api/freelancer/applications")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/freelancer/profile returns 200")
    void updateProfile() throws Exception {
        FreelancerProfile profile = new FreelancerProfile();
        profile.setBio("Test Bio");
        profile.setSkills(Arrays.asList("Java", "Spring"));
        profile.setWebsite("https://test.com");
        profile.setCreatedAt(LocalDateTime.now());
        when(freelancerService.updateProfile(anyLong(), any(FreelancerProfile.class))).thenReturn(profile);
        User user = new User(3L, "Freelancer", "freelancer@freelance.com", "freelancer123456", Role.FREELANCER, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        mockMvc.perform(put("/api/freelancer/profile")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/freelancer/profile returns 204")
    void deleteProfile() throws Exception {
        doNothing().when(freelancerService).deleteProfile(anyLong());
        User user = new User(3L, "Freelancer", "freelancer@freelance.com", "freelancer123456", Role.FREELANCER, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        mockMvc.perform(delete("/api/freelancer/profile")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
        )
                .andExpect(status().isNoContent());
    }
} 