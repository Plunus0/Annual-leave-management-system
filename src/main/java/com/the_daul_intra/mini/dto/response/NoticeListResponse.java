package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeListResponse {
    private Long id;
    private Long rowNum;
    private String title;
    private String regDate;
    private String writeId;
    private Long viewCount;
}
