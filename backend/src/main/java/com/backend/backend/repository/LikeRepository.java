package com.backend.backend.repository;

import com.backend.backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, String> {
    int countByTopic_Id(String topicId);
    boolean existsLikeByTopic_IdAndUser_Username(String topicId, String username);
}
