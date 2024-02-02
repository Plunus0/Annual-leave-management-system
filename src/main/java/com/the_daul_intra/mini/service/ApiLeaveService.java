package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.EmpDetails;
import com.the_daul_intra.mini.dto.entity.DetailsLeaveAbsence;
import com.the_daul_intra.mini.dto.entity.DetailsLeaveDate;
import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.request.ApiLeavePostRequest;
import com.the_daul_intra.mini.dto.request.ApiLeaveSearchRequest;
import com.the_daul_intra.mini.dto.response.ApiOffDetailResponse;
import com.the_daul_intra.mini.dto.response.ApiOffListItemResponse;
import com.the_daul_intra.mini.exception.AppException;
import com.the_daul_intra.mini.exception.ErrorCode;
import com.the_daul_intra.mini.repository.ApiDetailsLeaveAbsenceRepository;
import com.the_daul_intra.mini.repository.ApiDetailsLeaveDateRepository;
import com.the_daul_intra.mini.repository.ApiEmpLoginRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiLeaveService {

    private final ApiDetailsLeaveAbsenceRepository apiDetailsLeaveAbsenceRepository;
    private final ApiEmpLoginRepository apiEmpLoginRepository;
    private final ApiDetailsLeaveDateRepository apiDetailsLeaveDateRepository;
    Long empId = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    //휴가신청서 작성
    @Transactional
    public DetailsLeaveAbsence createLeaveRequest(ApiLeavePostRequest request) {

        //authentication객체에 SecurityContextHolder를 담아서 인증정보를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //authentication에서 empId 추출
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            empId = ((EmpDetails) authentication.getPrincipal()).getEmpId();
        }

        //인증정보를 바탕으로 empId확인 후 없다면 로그인페이지로 이동
        Employee employee = apiEmpLoginRepository.findById(empId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,"  로그인 내역이 존재하지 않습니다."));

        DetailsLeaveAbsence leaveRequest = DetailsLeaveAbsence.builder()
                .employee(employee)
                .absenceLeavePeriod((long) request.getUseDates().length)
                .absenceType(request.getType())
                .applicantComments(request.getReason())
                .applicationDate(LocalDateTime.now())
                .processingStatus("신청")
                .build();
        leaveRequest = apiDetailsLeaveAbsenceRepository.save(leaveRequest);
        for (LocalDate useDate : request.getUseDates()) {

            DetailsLeaveDate leaveDate = DetailsLeaveDate.builder()
                    .leaveRequest(leaveRequest)
                    .employee(employee)
                    .useDate(useDate)
                    .build();

            apiDetailsLeaveDateRepository.save(leaveDate);
        }
        return leaveRequest;
    }


    public List<ApiOffListItemResponse> searchLeavesList(ApiLeaveSearchRequest request) {

        //authentication객체에 SecurityContextHolder를 담아서 인증정보를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //authentication에서 empId 추출
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            empId = ((EmpDetails) authentication.getPrincipal()).getEmpId();
        }

        //검색 조건에 따른 검색 실행
        List<DetailsLeaveAbsence> leaves = apiDetailsLeaveAbsenceRepository.findAll(LeaveSpecifications.withCriteria(request, empId),Sort.by(Sort.Direction.DESC, "applicationDate"));

        //검색 결과 반환
        return leaves.stream().map(leave -> new ApiOffListItemResponse(
                leave.getId(),
                leave.getEmployee().getId(),
                leave.getApplicationDate().format(formatter),
                leave.getProcessingStatus(),
                leave.getAbsenceType()
        )).collect(Collectors.toList());
    }

    public ApiOffDetailResponse getLeaveDetails(Long requestId) {

        //authentication객체에 SecurityContextHolder를 담아서 인증정보를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //authentication에서 empId 추출
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            empId = ((EmpDetails) authentication.getPrincipal()).getEmpId();
        }

        DetailsLeaveAbsence leaveAbsence = apiDetailsLeaveAbsenceRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND," 해당 신청서가 존재하지 않습니다."));

        Set<DetailsLeaveDate> leaveDates = leaveAbsence.getLeaveDates();
        LocalDate[] useDates = leaveDates.stream()
                .map(DetailsLeaveDate::getUseDate)
                .toArray(LocalDate[]::new);

        Employee employee = leaveAbsence.getEmployee();

        return ApiOffDetailResponse.builder()
                .id(leaveAbsence.getId())
                .writerId(employee.getId())
                .writerName(employee.getEmployeeProfile().getName())
                .requestType(leaveAbsence.getAbsenceType())
                .status(leaveAbsence.getProcessingStatus())
                .leavePeriod(Long.toString(leaveAbsence.getAbsenceLeavePeriod()))
                .useDates(useDates)
                .regDate(leaveAbsence.getApplicationDate() != null ? leaveAbsence.getApplicationDate().format(formatter) : null)
                .receiveDate(leaveAbsence.getReceptionDate() != null ? leaveAbsence.getReceptionDate().format(formatter) : null)
                .confirmDate(leaveAbsence.getProcessedDate() != null ? leaveAbsence.getProcessedDate().format(formatter) : null)
                .receiveAdmin(leaveAbsence.getReceptionAdmin() != null && leaveAbsence.getReceptionAdmin().getEmployeeProfile() != null ? leaveAbsence.getReceptionAdmin().getEmployeeProfile().getName() : null)
                .confirmAdmin(leaveAbsence.getProcessedAdmin() != null && leaveAbsence.getProcessedAdmin().getEmployeeProfile() != null ? leaveAbsence.getProcessedAdmin().getEmployeeProfile().getName() : null)
                .reason(leaveAbsence.getApplicantComments())
                .adminComment(leaveAbsence.getAdminComment())
                .build();
    }
}
