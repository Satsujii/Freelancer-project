package com.freelance.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freelance.marketplace.dto.AuthResponse;
import com.freelance.marketplace.dto.LoginRequest;
import com.freelance.marketplace.entity.Role;
import com.freelance.marketplace.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public AuthService mockAuthService() {
            return Mockito.mock(AuthService.class);
        }
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("anas@aaa.com", "123456789");
        AuthResponse authResponse = AuthResponse.builder()
                .token("jwt-token-123")
                .id(1L)
                .email("anas@aaa.com")
                .name("Anas")
                .role(Role.FREELANCER)
                .authorities(List.of("ROLE_FREELANCER"))
                .build();

        // When
        when(authService.login(Mockito.any())).thenReturn(authResponse);

        // Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"))
                .andExpect(jsonPath("$.email").value("anas@aaa.com"))
                .andExpect(jsonPath("$.role").value("FREELANCER"));
    }
}
