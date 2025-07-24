package com.freelance.marketplace.service;

import com.freelance.marketplace.entity.ClientProfile;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.repository.ClientProfileRepository;
import com.freelance.marketplace.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;

class ClientProfileServiceTest {
    @Mock
    private ClientProfileRepository clientRepo;
    @Mock
    private UserRepository userRepo;
    @InjectMocks
    private ClientProfileService service;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        service = new ClientProfileService();
        // Use reflection to inject mocks into private fields
        Field clientRepoField = ClientProfileService.class.getDeclaredField("clientRepo");
        clientRepoField.setAccessible(true);
        clientRepoField.set(service, clientRepo);
        Field userRepoField = ClientProfileService.class.getDeclaredField("userRepo");
        userRepoField.setAccessible(true);
        userRepoField.set(service, userRepo);
    }

    @Test
    void createProfile_success() {
        User user = new User(); user.setId(1L);
        ClientProfile profile = new ClientProfile();
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(clientRepo.save(any(ClientProfile.class))).thenAnswer(inv -> inv.getArgument(0));
        ClientProfile result = service.createProfile(1L, profile);
        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    void createProfile_userNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.createProfile(1L, new ClientProfile()));
    }

    @Test
    void getProfile_success() {
        ClientProfile profile = new ClientProfile();
        when(clientRepo.findByUserId(1L)).thenReturn(Optional.of(profile));
        ClientProfile result = service.getProfile(1L);
        assertNotNull(result);
    }

    @Test
    void getProfile_notFound() {
        when(clientRepo.findByUserId(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getProfile(1L));
    }

    @Test
    void updateProfile_success() {
        ClientProfile existing = new ClientProfile();
        when(clientRepo.findByUserId(1L)).thenReturn(Optional.of(existing));
        when(clientRepo.save(any(ClientProfile.class))).thenAnswer(inv -> inv.getArgument(0));
        ClientProfile updated = new ClientProfile();
        updated.setBio("bio");
        updated.setCompanyName("company");
        updated.setWebsite("site");
        ClientProfile result = service.updateProfile(1L, updated);
        assertEquals("bio", result.getBio());
        assertEquals("company", result.getCompanyName());
        assertEquals("site", result.getWebsite());
    }

    @Test
    void updateProfile_notFound() {
        when(clientRepo.findByUserId(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.updateProfile(1L, new ClientProfile()));
    }
} 