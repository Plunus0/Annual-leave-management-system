package com.the_daul_intra.mini.dto.request;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.Notice;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoticePostRequest {
    private String title;
    private String content;
}
