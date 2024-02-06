package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IndexNoticeListResponse {
    private Long noticeListId;
    private String noticeListTitle;
    private String noticeListRegDate;
}
