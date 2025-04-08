package com.backend.backend.repository;

import com.backend.backend.entity.Profile;
import com.backend.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    Optional<Profile> findProfileByUser_Username(String userName);
}
