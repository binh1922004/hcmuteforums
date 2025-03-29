package com.backend.backend.repository;

import com.backend.backend.entity.TopicImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TopicImageRepository extends JpaRepository<TopicImage, String> {
}
