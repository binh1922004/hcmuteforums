package com.backend.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    List<String> recipients;
    List<String> ccList;
    List<String> bccList;
    String subject;
    String body;
    Boolean isHtml;
    String attachmentPath;
}
