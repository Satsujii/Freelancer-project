package com.freelance.marketplace.controller;

import com.freelance.marketplace.service.JobService;
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
import java.util.Collections;
import org.springframework.security.test.context.support.WithMockUser;
import com.freelance.marketplace.security.CustomUserPrincipal;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.entity.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

@WithMockUser(username = "client", roles = {"CLIENT"})
@SpringBootTest
@AutoConfigureMockMvc
class JobControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JobService jobService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        public JobService jobService() {
            return org.mockito.Mockito.mock(JobService.class);
        }
    }

    @Test
    @DisplayName("GET /api/jobs/public returns 200")
    void getAllPublicJobs() throws Exception {
        when(jobService.getAllJobs()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/jobs/public"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/jobs returns 200")
    void getAllJobs() throws Exception {
        when(jobService.getAllJobs()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/jobs/{id} returns 200")
    void getJob() throws Exception {
        when(jobService.getJob(anyLong())).thenReturn(null);
        mockMvc.perform(get("/api/jobs/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/jobs returns 200")
    void createJob() throws Exception {
        when(jobService.createJob(any(), anyLong())).thenReturn(null);
        User user = new User(1L, "Test Client", "client@example.com", "password", Role.CLIENT, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        mockMvc.perform(post("/api/jobs")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/jobs/{id} returns 200")
    void updateJob() throws Exception {
        when(jobService.updateJob(anyLong(), any(), anyLong())).thenReturn(null);
        User user = new User(1L, "Test Client", "client@example.com", "password", Role.CLIENT, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        mockMvc.perform(put("/api/jobs/1")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/jobs/{id} returns 204")
    void deleteJob() throws Exception {
        doNothing().when(jobService).deleteJob(anyLong(), anyLong());
        User user = new User(1L, "Test Client", "client@example.com", "password", Role.CLIENT, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        mockMvc.perform(delete("/api/jobs/1")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
        )
                .andExpect(status().isNoContent());
    }
} 