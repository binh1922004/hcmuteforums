package com.backend.backend.repository;

import com.backend.backend.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TopicRepository extends JpaRepository<Topic, String> {
    List<Topic> getTopicsBySubCategory_Id(String subCategoryId);
}
