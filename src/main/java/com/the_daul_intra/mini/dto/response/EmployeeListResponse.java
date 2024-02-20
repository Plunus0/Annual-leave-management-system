package com.the_daul_intra.mini.dto.response;

import com.the_daul_intra.mini.dto.entity.YesNo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeListResponse {
    private Long id;
    private Long rowNum;
    private String position;
    private String name;
    private String contactInfo;
    private String email;
    private YesNo projectStatus;
}
