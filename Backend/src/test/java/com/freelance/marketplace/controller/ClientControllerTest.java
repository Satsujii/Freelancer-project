package com.freelance.marketplace.controller;

import com.freelance.marketplace.service.ClientProfileService;
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
import com.freelance.marketplace.entity.ClientProfile;

@SpringBootTest
@AutoConfigureMockMvc
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
    @DisplayName("GET /api/clients/profile returns 200")
    void getProfile() throws Exception {
        when(clientService.getProfile(anyLong())).thenReturn(new ClientProfile());
        User user = new User(2L, "Client", "client@client.com", "client123456", Role.CLIENT, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        mockMvc.perform(get("/api/clients/profile")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/clients/applications returns 200")
    void getApplicationsForMyJobs() throws Exception {
        when(jobApplicationService.getApplicationsForClient(anyLong())).thenReturn(Collections.emptyList());
        User user = new User(2L, "Client", "client@client.com", "client123456", Role.CLIENT, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        mockMvc.perform(get("/api/clients/applications")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/clients/profile returns 200")
    void updateProfile() throws Exception {
        when(clientService.updateProfile(anyLong(), any())).thenReturn(new ClientProfile());
        User user = new User(2L, "Client", "client@client.com", "client123456", Role.CLIENT, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        mockMvc.perform(put("/api/clients/profile")
                .with(authentication(auth))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }
} 