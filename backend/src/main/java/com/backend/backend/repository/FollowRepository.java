package com.backend.backend.repository;

import com.backend.backend.entity.Follow;
import com.backend.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, String> {
    boolean existsByFollowerAndFollowed(User follower, User followed);
    boolean existsByFollower_UsernameAndFollowed_Username(String follower, String followed);
    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);
    Page<Follow> findAllByFollowed(User followed, Pageable pageable);
    Page<Follow> findAllByFollower(User follower, Pageable pageable);
}
