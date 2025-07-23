package com.freelance.marketplace.controller;

import com.freelance.marketplace.service.ClientProfileService;
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

@WebMvcTest(ClientController.class)
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientProfileService clientService;
    @Autowired
    private JobApplicationService jobApplicationService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        public ClientProfileService clientProfileService() {
            return org.mockito.Mockito.mock(ClientProfileService.class);
        }
        @Bean
        @Primary
        public JobApplicationService jobApplicationService() {
            return org.mockito.Mockito.mock(JobApplicationService.class);
        }
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("GET /api/clients/profile returns 200")
    void getProfile() throws Exception {
        when(clientService.getProfile(anyLong())).thenReturn(null);
        mockMvc.perform(get("/api/clients/profile"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("GET /api/clients/applications returns 200")
    void getApplicationsForMyJobs() throws Exception {
        when(jobApplicationService.getApplicationsForClient(anyLong())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/clients/applications"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("PUT /api/clients/profile returns 200")
    void updateProfile() throws Exception {
        when(clientService.updateProfile(anyLong(), any())).thenReturn(null);
        mockMvc.perform(put("/api/clients/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }
} 