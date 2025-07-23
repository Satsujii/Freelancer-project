package com.freelance.marketplace.controller;

import com.freelance.marketplace.service.JobApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collections;
import com.freelance.marketplace.security.CustomUserPrincipal;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.entity.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import com.freelance.marketplace.dto.JobApplicationDto;

@WithMockUser(username = "freelancer", roles = {"FREELANCER"})
@SpringBootTest
@AutoConfigureMockMvc
class JobApplicationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JobApplicationService jobApplicationService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        public JobApplicationService jobApplicationService() {
            return org.mockito.Mockito.mock(JobApplicationService.class);
        }
    }

    @Test
    @DisplayName("POST /api/applications/apply/{jobId} returns 200")
    void applyForJob() throws Exception {
        when(jobApplicationService.applyForJob(anyLong(), anyLong())).thenReturn(new JobApplicationDto());
        User user = new User(5L, "Freelancer", "freelancer@freelance.com", "freelancer123456", Role.FREELANCER, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        mockMvc.perform(post("/api/applications/apply/1")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("GET /api/applications/job/{jobId} returns 200")
    void getApplicationsForJob() throws Exception {
        when(jobApplicationService.getApplicationsForJob(anyLong())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/applications/job/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("POST /api/applications/accept/{applicationId} returns 200")
    void acceptApplication() throws Exception {
        when(jobApplicationService.acceptApplication(anyLong())).thenReturn(null);
        mockMvc.perform(post("/api/applications/accept/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("POST /api/applications/reject/{applicationId} returns 200")
    void rejectApplication() throws Exception {
        when(jobApplicationService.rejectApplication(anyLong())).thenReturn(null);
        mockMvc.perform(post("/api/applications/reject/1"))
                .andExpect(status().isOk());
    }
} 