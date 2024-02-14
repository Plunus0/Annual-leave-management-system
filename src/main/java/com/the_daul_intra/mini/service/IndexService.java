package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.dto.entity.YesNo;
import com.the_daul_intra.mini.dto.response.IndexNoticeListResponse;
import com.the_daul_intra.mini.dto.response.IndexResponse;
import com.the_daul_intra.mini.repository.ApiDetailsLeaveAbsenceRepository;
import com.the_daul_intra.mini.repository.ApiDetailsLeaveDateRepository;
import com.the_daul_intra.mini.repository.EmployeeRepository;
import com.the_daul_intra.mini.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexService {
    private final NoticeRepository noticeRepository;
    private final ApiDetailsLeaveAbsenceRepository apiDetailsLeaveAbsenceRepository;
    private final ApiDetailsLeaveDateRepository apiDetailsLeaveDateRepository;
    private final EmployeeRepository employeeRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public IndexResponse getIndexData(){
        IndexResponse response = new IndexResponse();
        LocalDateTime today = LocalDateTime.now();
        LocalDate todayDate = LocalDate.now();

        // 최근 공지사항 조회
        Notice latestNotice = noticeRepository.findFirstByOrderByRegDateDesc();
        if (latestNotice != null) {
            response.setNoticeId(latestNotice.getId());
            response.setNoticeTitle(latestNotice.getTitle());
            response.setNoticeContent(latestNotice.getContent());
            response.setNoticeRegDate(latestNotice.getRegDate().format(formatter));
        }

        // 공지사항 목록 조회
        List<Notice> notices = noticeRepository.findTop5ByOrderByRegDateDesc();
        IndexNoticeListResponse[] noticeListResponses = notices.stream()
                .map(notice -> new IndexNoticeListResponse(notice.getId(), notice.getTitle(), notice.getRegDate().format(formatter)))
                .toArray(IndexNoticeListResponse[]::new);
        response.setNoticeLists(noticeListResponses);

        // 휴가신청 집계 로직
        response.setRequestOffCount(apiDetailsLeaveAbsenceRepository.countByProcessingStatusNot("승인"));
        response.setRequestOffAnnuCount(apiDetailsLeaveAbsenceRepository.countByProcessingStatusNotAndAbsenceType("승인", "연차"));
        response.setRequestOffHalfAMCount(apiDetailsLeaveAbsenceRepository.countByProcessingStatusNotAndAbsenceType("승인", "반차(오전)"));
        response.setRequestOffHalfPMCount(apiDetailsLeaveAbsenceRepository.countByProcessingStatusNotAndAbsenceType("승인", "반차(오후)"));
        response.setRequestOffSickCount(apiDetailsLeaveAbsenceRepository.countByProcessingStatusNotAndAbsenceType("승인", "병가"));
        response.setRequestOffEtcCount(apiDetailsLeaveAbsenceRepository.countByProcessingStatusNot("승인")
                - response.getRequestOffAnnuCount()
                - response.getRequestOffHalfAMCount()
                - response.getRequestOffHalfPMCount()
                - response.getRequestOffSickCount());

        // 투입현황 데이터 계산
        response.setToday(todayDate.toString());
        response.setRightEmpCount(employeeRepository.countByAdminStatusAndRetirementDateBefore(YesNo.N, today));
        response.setRightEmpProjectYCount(employeeRepository.countByAdminStatusAndRetirementDateBeforeAndProjectStatusY(YesNo.N, today, YesNo.Y));
        response.setRightEmpProjectNCount(employeeRepository.countByAdminStatusAndRetirementDateBeforeAndProjectStatusN(YesNo.N, today, YesNo.N));

        // 특정 absenceType에 대한 개수 계산
        response.setRightOffCount(apiDetailsLeaveDateRepository.countByUseDate(todayDate));
        response.setRightOffAnnuCount(apiDetailsLeaveDateRepository.countByUseDateAndAbsenceType(todayDate, "연차"));
        response.setRightOffHalfAMCount(apiDetailsLeaveDateRepository.countByUseDateAndAbsenceType(todayDate, "반차(오전)"));
        response.setRightOffHalfPMCount(apiDetailsLeaveDateRepository.countByUseDateAndAbsenceType(todayDate, "반차(오후)"));
        response.setRightOffSickCount(apiDetailsLeaveDateRepository.countByUseDateAndAbsenceType(todayDate, "병가"));
        response.setRightOffEtcCount(apiDetailsLeaveDateRepository.countByUseDate(todayDate)
                -response.getRightOffAnnuCount()
                -response.getRightOffHalfAMCount()
                -response.getRightOffHalfPMCount()
                -response.getRightOffSickCount());

        return response;
    }
}
