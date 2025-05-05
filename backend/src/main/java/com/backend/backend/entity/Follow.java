package com.backend.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "follows")

public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;  //Nguoi theo doi

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;  //Nguoi duoc theo doi

    private String status;

    private Date createdAt;
}
