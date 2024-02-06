package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RightSideMenuResponse {
    private String today;

    private Integer rightEmpCount;

    private Integer rightEmpWorkCount;
    private Integer rightEmpAbsentCount;

    private Integer rightEmpProjectYCount;
    private Integer rightEmpProjectNCount;

    private Integer rightOffCount;
    private Integer rightOffAnnuCount;
    private Integer rightOffHalfAMCount;
    private Integer rightOffHalfPMCount;
    private Integer rightOffSickCount;
    private Integer rightOffEtcCount;
}
