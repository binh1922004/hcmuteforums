package com.backend.backend.repository;

import com.backend.backend.entity.Profile;
import com.backend.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    Optional<Profile> findProfileByUser_Username(String userName);
    @Query("select p.avatarUrl from Profile p where p.user.username = :userName ")
    String findAvatarUrlByUserName(@Param("userName") String userName);
}
