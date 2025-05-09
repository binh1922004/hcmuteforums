package com.backend.backend.repository;

import com.backend.backend.entity.Notification;
import com.backend.backend.entity.Profile;
import com.backend.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    Page<Notification> findAllBySendUser(User sendUser, Pageable pageable);
    Page<Notification> findALlByRecieveUser(User recieveUser, Pageable pageable);
}
