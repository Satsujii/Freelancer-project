package com.freelance.marketplace.service;

import com.freelance.marketplace.dto.FreelancerProfileRequest;
import com.freelance.marketplace.dto.FreelancerProfileResponse;
import com.freelance.marketplace.entity.FreelancerProfile;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.repository.FreelancerProfileRepository;
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
import java.util.Arrays;

class FreelancerProfileServiceTest {
    @Mock
    private FreelancerProfileRepository freelancerRepo;
    @Mock
    private UserRepository userRepo;
    @InjectMocks
    private FreelancerProfileService service;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        service = new FreelancerProfileService();
        Field freelancerRepoField = FreelancerProfileService.class.getDeclaredField("freelancerRepo");
        freelancerRepoField.setAccessible(true);
        freelancerRepoField.set(service, freelancerRepo);
        Field userRepoField = FreelancerProfileService.class.getDeclaredField("userRepo");
        userRepoField.setAccessible(true);
        userRepoField.set(service, userRepo);
    }

    @Test
    void createProfile_success() {
        FreelancerProfileRequest req = new FreelancerProfileRequest();
        req.setBio("bio");
        req.setSkills(Arrays.asList("skill1", "skill2"));
        req.setWebsite("site");
        User user = new User(); user.setId(1L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(freelancerRepo.existsByUserId(1L)).thenReturn(false);
        when(freelancerRepo.save(any(FreelancerProfile.class))).thenAnswer(inv -> inv.getArgument(0));
        FreelancerProfileResponse resp = service.createProfile(1L, req);
        assertNotNull(resp);
        assertEquals("bio", resp.getBio());
    }

    @Test
    void createProfile_alreadyExists() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(new User()));
        when(freelancerRepo.existsByUserId(1L)).thenReturn(true);
        assertThrows(RuntimeException.class, () -> service.createProfile(1L, new FreelancerProfileRequest()));
    }

    @Test
    void updateProfile_success() {
        FreelancerProfile profile = new FreelancerProfile();
        profile.setBio("old");
        // Set a non-null User to avoid NPE
        User user = new User();
        user.setId(1L);
        profile.setUser(user);
        when(freelancerRepo.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(freelancerRepo.save(any(FreelancerProfile.class))).thenAnswer(inv -> inv.getArgument(0));
        FreelancerProfileRequest req = new FreelancerProfileRequest();
        req.setBio("new");
        req.setSkills(Arrays.asList("skill1", "skill2"));
        req.setWebsite("site");
        FreelancerProfileResponse resp = service.updateProfile(1L, req);
        assertEquals("new", resp.getBio());
    }

    @Test
    void updateProfile_notFound() {
        when(freelancerRepo.findByUserId(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.updateProfile(1L, new FreelancerProfileRequest()));
    }

    @Test
    void getProfileByUserId_success() {
        FreelancerProfile profile = new FreelancerProfile();
        profile.setBio("bio");
        profile.setSkills(Arrays.asList("skill1", "skill2"));
        profile.setWebsite("site");
        profile.setId(1L);
        User user = new User(); user.setId(2L);
        profile.setUser(user);
        when(freelancerRepo.findByUserId(2L)).thenReturn(Optional.of(profile));
        FreelancerProfileResponse resp = service.getProfileByUserId(2L);
        assertEquals("bio", resp.getBio());
        assertEquals(Arrays.asList("skill1", "skill2"), resp.getSkills());
        assertEquals("site", resp.getWebsite());
        assertEquals(2L, resp.getUserId());
    }

    @Test
    void getProfileByUserId_notFound() {
        when(freelancerRepo.findByUserId(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getProfileByUserId(2L));
    }
} 