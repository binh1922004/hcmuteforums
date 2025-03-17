package com.backend.backend.repository;

import com.backend.backend.entity.Reply;
import com.backend.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, String> {
    int countByTopic_Id(String topicId);
    Reply findRepliesById(String id);
    boolean existsRepliesByIdAndUser_Username(String id, String username);
    void deleteRepliesByParentReplyId(String id);
}
