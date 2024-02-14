package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IndexNoticeListResponse {
    private Long noticeListId; // 공지사항 id
    private String noticeListTitle; // 공지사항 제목
    private String noticeListRegDate; // 공지사항 작성일
}
