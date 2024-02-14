package com.the_daul_intra.mini.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexResponse {
    //최근 공지사항(메인)
    private Long noticeId; // 공지사항 id
    private String noticeTitle; // 공지사항 제목
    private String noticeContent; // 공지사항 내용
    private String noticeRegDate; // 공지사항 작성일

    //공지사항 목록
    private IndexNoticeListResponse[] noticeLists;

    //휴가신청 집계
    private Integer requestOffCount; // 총 휴가신청 수
    private Integer requestOffAnnuCount; // 총 연차신청 수
    private Integer requestOffHalfAMCount; // 총 반차(오전)신청 수
    private Integer requestOffHalfPMCount; // 총 반차(오후)신청 수
    private Integer requestOffSickCount; // 총 병가신청 수
    private Integer requestOffEtcCount; // 총 그 외 신청 수

    //투입현황
    private String today; // 오늘 날자
    private Integer rightEmpCount; // 관리자를 제외한 총 재직인원 수
    private Integer rightEmpWorkCount; // 관리자를 제외한 총 출근인원 수
    private Integer rightEmpAbsentCount; // 관리자를 제외한 총 결근인원 수
    private Integer rightEmpProjectYCount; // 관리자를 제외한 총 프로젝트 투입인원 수
    private Integer rightEmpProjectNCount; // 관리자를 제외한 총 프로젝트 미투입인원 수
    private Integer rightOffCount; // 관리자를 제외한 총 휴가 중인 수
    private Integer rightOffAnnuCount; // 관리자를 제외한 총 연차 중인 수
    private Integer rightOffHalfAMCount; // 관리자를 제외한 총 반차(오전) 중인 수
    private Integer rightOffHalfPMCount; // 관리자를 제외한 총 반차(오후) 중인 수
    private Integer rightOffSickCount; // 관리자를 제외한 총 병가 중인 수
    private Integer rightOffEtcCount; // 관리자를 제외한 총 그 외 수
}
