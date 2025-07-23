package com.freelance.marketplace.controller;

import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.entity.JobPost;
import com.freelance.marketplace.service.AdminService;
import com.freelance.marketplace.service.JwtService;
import com.freelance.marketplace.service.CustomUserDetailsService;
import com.freelance.marketplace.repository.UserRepository;
import com.freelance.marketplace.security.CustomUserPrincipal;
import com.freelance.marketplace.entity.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

@WithMockUser(username = "admin", roles = {"ADMIN"})
@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdminService adminService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        public AdminService adminService() {
            return org.mockito.Mockito.mock(AdminService.class);
        }
        
        @Bean
        @Primary
        public JwtService jwtService() {
            return org.mockito.Mockito.mock(JwtService.class);
        }

        @Bean
        @Primary
        public CustomUserDetailsService customUserDetailsService() {
            return org.mockito.Mockito.mock(CustomUserDetailsService.class);
        }
    }

    @Test
    @DisplayName("GET /api/admin/users returns 200")
    void getAllUsers() throws Exception {
        when(adminService.getAllUsers()).thenReturn(Collections.emptyList());
        User user = new User(1L, "Admin", "admin@admin.com", "admin123456", Role.ADMIN, null, null, true);
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        mockMvc.perform(get("/api/admin/users")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/admin/jobs returns 200")
    void getAllJobs() throws Exception {
        when(adminService.getAllJobs()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/admin/jobs"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/admin/statistics returns 200")
    void getJobStatistics() throws Exception {
        when(adminService.getJobStatistics()).thenReturn(Collections.emptyMap());
        mockMvc.perform(get("/api/admin/statistics"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/admin/statistics/total-jobs returns 200")
    void getTotalJobs() throws Exception {
        when(adminService.getTotalJobs()).thenReturn(0L);
        mockMvc.perform(get("/api/admin/statistics/total-jobs"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/admin/users/{id} returns 204")
    void deleteUser() throws Exception {
        doNothing().when(adminService).deleteUser(1L);
        mockMvc.perform(delete("/api/admin/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /api/admin/users/{id} returns 200")
    void updateUser() throws Exception {
        User user = new User();
        when(adminService.updateUser(eq(1L), any(User.class))).thenReturn(user);
        mockMvc.perform(put("/api/admin/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/admin/jobs/{id} returns 204")
    void deleteJob() throws Exception {
        doNothing().when(adminService).deleteJob(1L);
        mockMvc.perform(delete("/api/admin/jobs/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /api/admin/jobs/{id} returns 200")
    void updateJob() throws Exception {
        JobPost job = new JobPost();
        when(adminService.updateJob(eq(1L), any(JobPost.class))).thenReturn(job);
        mockMvc.perform(put("/api/admin/jobs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }
} 