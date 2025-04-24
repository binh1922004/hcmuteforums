package com.backend.backend.repository;

import com.backend.backend.entity.Reply;
import com.backend.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, String> {
    boolean existsById(String id);
    int countByTopic_Id(String topicId);
    Reply findRepliesById(String id);
    boolean existsRepliesByIdAndUser_Username(String id, String username);
    void deleteRepliesByParentReplyId(String id);
    Page<Reply> getAllByTopic_Id(String topicId, Pageable pageable);

    //get all reply which is parent
    @Query("SELECT r FROM Reply r WHERE r.topic.id = :topicId AND r.parentReplyId = ''")
    Page<Reply> findByTopic_IdAndParentReplyIdIsNull(String topicId, Pageable pageable);

    Page<Reply> findByParentReplyId(String parentReplyId, Pageable pageable);
}
