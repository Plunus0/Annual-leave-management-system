package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IndexResponse {
    private Long noticeId;
    private String noticeTitle;
    private String noticeContent;
    private String noticeRegDate;

    private IndexNoticeListResponse[] noticeLists;

    private Integer requestOffCount;
    private Integer requestOffAnnuCount;
    private Integer requestOffHalfAMCount;
    private Integer requestOffHalfPMCount;
    private Integer requestOffSickCount;
    private Integer requestOffEtcCount;
}
