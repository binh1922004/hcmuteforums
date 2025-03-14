package com.backend.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicUpdateRequest {
    String title;
    String content;
    String subCategoryId;
}
