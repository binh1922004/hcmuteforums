package com.backend.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Replies")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String parentReplyId;

    @Column(name = "create_at")
    private Date createdAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
    // Optional - for easier access to child replies
    @OneToMany(mappedBy = "parentReplyId", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reply> childReplies;
}
