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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "action_id")
    private String actionId;

    private NotificationContent content;

    @Column(name = "create_at")
    private Date createdAt;

    //user send
    @ManyToOne
    @JoinColumn(name = "send_user_id", nullable = false)
    private User sendUser;
    //user received
    @ManyToOne
    @JoinColumn(name = "received_user_id", nullable = false)
    private User recieveUser;
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
}
