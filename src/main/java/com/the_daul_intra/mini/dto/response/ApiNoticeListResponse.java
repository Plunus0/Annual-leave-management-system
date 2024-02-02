package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiNoticeListResponse {
    private List<ApiNoticeListItemResponse> content;
    private int pageNumber;
    private int pageSize;

/*  필요시 주석 해제 후 사용
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private boolean empty;*/
}
