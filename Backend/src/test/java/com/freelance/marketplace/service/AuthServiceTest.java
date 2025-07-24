package com.freelance.marketplace.service;

import com.freelance.marketplace.dto.AuthResponse;
import com.freelance.marketplace.dto.LoginRequest;
import com.freelance.marketplace.dto.RegisterRequest;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.entity.Role;
import com.freelance.marketplace.repository.UserRepository;
import com.freelance.marketplace.repository.ClientProfileRepository;
import com.freelance.marketplace.repository.FreelancerProfileRepository;
import com.freelance.marketplace.service.AdminService;
import com.freelance.marketplace.service.JwtService;
import com.freelance.marketplace.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;

class AuthServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private ClientProfileRepository clientRepo;
    @Mock private FreelancerProfileRepository freelancerRepo;
    @Mock private AdminService adminService;
    @Mock private Authentication authentication;
    @Mock private UserDetails userDetails;
    @InjectMocks private AuthService authService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService();
        // Use reflection to inject mocks into private fields
        Field userRepoField = AuthService.class.getDeclaredField("userRepository");
        userRepoField.setAccessible(true);
        userRepoField.set(authService, userRepository);
        Field passwordEncoderField = AuthService.class.getDeclaredField("passwordEncoder");
        passwordEncoderField.setAccessible(true);
        passwordEncoderField.set(authService, passwordEncoder);
        Field jwtServiceField = AuthService.class.getDeclaredField("jwtService");
        jwtServiceField.setAccessible(true);
        jwtServiceField.set(authService, jwtService);
        Field authenticationManagerField = AuthService.class.getDeclaredField("authenticationManager");
        authenticationManagerField.setAccessible(true);
        authenticationManagerField.set(authService, authenticationManager);
        Field clientRepoField = AuthService.class.getDeclaredField("clientRepo");
        clientRepoField.setAccessible(true);
        clientRepoField.set(authService, clientRepo);
        Field freelancerRepoField = AuthService.class.getDeclaredField("freelancerRepo");
        freelancerRepoField.setAccessible(true);
        freelancerRepoField.set(authService, freelancerRepo);
        Field adminServiceField = AuthService.class.getDeclaredField("adminService");
        adminServiceField.setAccessible(true);
        adminServiceField.set(authService, adminService);
    }

    @Test
    void login_success() {
        LoginRequest req = new LoginRequest();
        req.setEmail("test@email.com");
        req.setPassword("pass");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("token");
        User user = new User();
        user.setId(1L); user.setEmail("test@email.com"); user.setName("Test"); user.setRole(Role.CLIENT);
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));
        when(userDetails.getAuthorities()).thenReturn(new java.util.ArrayList<>());
        AuthResponse resp = authService.login(req);
        assertNotNull(resp);
        assertEquals("token", resp.getToken());
        assertEquals(1L, resp.getId());
    }

    @Test
    void login_userNotFound() {
        LoginRequest req = new LoginRequest();
        req.setEmail("notfound@email.com");
        req.setPassword("pass");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail("notfound@email.com")).thenReturn(Optional.empty());
        when(jwtService.generateToken(userDetails)).thenReturn("token");
        when(userDetails.getAuthorities()).thenReturn(new java.util.ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> authService.login(req));
    }

    @Test
    void register_emailTaken() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("taken@email.com");
        when(userRepository.existsByEmail("taken@email.com")).thenReturn(true);
        assertThrows(Exception.class, () -> authService.register(req));
    }
} 