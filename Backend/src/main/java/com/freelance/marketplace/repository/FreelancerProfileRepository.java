package com.freelance.marketplace.repository;

import com.freelance.marketplace.entity.FreelancerProfile;
import com.freelance.marketplace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {
    Optional<FreelancerProfile> findByUser(User user);
    Optional<FreelancerProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
