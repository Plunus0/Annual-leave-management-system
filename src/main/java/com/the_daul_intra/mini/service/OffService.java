package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.EmpDetails;
import com.the_daul_intra.mini.dto.entity.DetailsLeaveAbsence;
import com.the_daul_intra.mini.dto.entity.DetailsLeaveDate;
import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.dto.request.ReceptRequest;
import com.the_daul_intra.mini.dto.response.OffDetailResponse;
import com.the_daul_intra.mini.dto.response.OffListResponse;
import com.the_daul_intra.mini.exception.AppException;
import com.the_daul_intra.mini.exception.ErrorCode;
import com.the_daul_intra.mini.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OffService {

    private final ApiDetailsLeaveAbsenceRepository apiDetailsLeaveAbsenceRepository;
    private final ApiDetailsLeaveDateRepository apiDetailsLeaveDateRepository;
    private final ApiEmpLoginRepository apiEmpLoginRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    Long empId = null;
    DateTimeFormatter formatter;

/*    public List<OffListResponse> getOffSearchList(String absenceType, String status) {
        Specification<DetailsLeaveAbsence> spec = LeaveSpecifications.withAdminCriteria(absenceType, status);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return apiDetailsLeaveAbsenceRepository.findAll(spec).stream()
                .map(offList -> new OffListResponse(
                        offList.getId(),
                        offList.getEmployee().getEmployeeProfile().getName(),
                        offList.getEmployee().getEmployeeProfile().getContactInformation(),
                        formatter.format(offList.getApplicationDate()),
                        offList.getAbsenceType(),
                        offList.getApplicantComments(),
                        offList.getProcessingStatus()
                )).collect(Collectors.toList());
    }*/
    public Page<OffListResponse> getOffSearchList(Integer page, Integer size, String absenceType, String status) {
        Specification<DetailsLeaveAbsence> spec = LeaveSpecifications.withAdminCriteria(absenceType, status);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("applicationDate").ascending());
        //휴가신청서 리스트 조회
        Page<DetailsLeaveAbsence> offPages = apiDetailsLeaveAbsenceRepository.findAll(spec, pageable);
        //휴가신청서 총 개수
        long totalList = offPages.getTotalElements();
        // 현재 페이지의 첫 번째 공지사항 번호
        AtomicInteger startNumber = new AtomicInteger((int) totalList - (page - 1) * size);

        return offPages.map(offList -> new OffListResponse(
                offList.getId(),
                (long) startNumber.getAndDecrement(),
                offList.getEmployee().getEmployeeProfile().getEmployeeNumber(),
                offList.getEmployee().getEmployeeProfile().getName(),
                offList.getEmployee().getEmployeeProfile().getContactInformation(),
                offList.getApplicationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                offList.getAbsenceType(),
                offList.getApplicantComments(),
                offList.getProcessingStatus()
        ));
    }

    public OffDetailResponse getOffDetail(Long id) {
        DetailsLeaveAbsence leaveAbsence = apiDetailsLeaveAbsenceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND," 해당 신청서가 존재하지 않습니다."));

        formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");

        Employee employee = leaveAbsence.getEmployee();
        EmployeeProfile employeeProfile = employee.getEmployeeProfile();

        String receiptAdminName = Optional.ofNullable(leaveAbsence.getReceptionAdmin())
                .map(Employee::getEmployeeProfile)
                .map(EmployeeProfile::getName)
                .orElse(null);

        String processedAdminName = Optional.ofNullable(leaveAbsence.getProcessedAdmin())
                .map(Employee::getEmployeeProfile)
                .map(EmployeeProfile::getName)
                .orElse(null);

        LocalDate[] useDates = leaveAbsence.getLeaveDates().stream()
                .map(DetailsLeaveDate::getUseDate)
                .sorted()
                .toArray(LocalDate[]::new);

        return OffDetailResponse.builder()
                .employeeNumber(employeeProfile.getEmployeeNumber())
                .id(leaveAbsence.getId())
                .writerId(employee.getId())
                .writerName(employeeProfile.getName())
                .useDates(useDates)
                .leavePeriod((long) useDates.length)
                .requestType(leaveAbsence.getAbsenceType())
                .status(leaveAbsence.getProcessingStatus())
                .regDate(formatter.format(leaveAbsence.getApplicationDate()))
                .receiveDate(leaveAbsence.getReceptionDate() != null ? formatter.format(leaveAbsence.getReceptionDate()) : null)
                .confirmDate(leaveAbsence.getProcessedDate() != null ? formatter.format(leaveAbsence.getProcessedDate()) : null)
                .receiveAdmin(receiptAdminName)
                .confirmAdmin(processedAdminName)
                .reason(leaveAbsence.getApplicantComments())
                .adminComment(leaveAbsence.getAdminComment())
                .build();
    }

    public void deleteLeaveAbsence(Long id) {
        DetailsLeaveAbsence leaveAbsence = apiDetailsLeaveAbsenceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND," 해당 신청서가 존재하지 않습니다."));
        apiDetailsLeaveDateRepository.deleteAll(leaveAbsence.getLeaveDates());
        apiDetailsLeaveAbsenceRepository.delete(leaveAbsence);
    }

    public void offRecept(Long id, ReceptRequest request) {

        //authentication객체에 SecurityContextHolder를 담아서 인증정보를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //authentication에서 empId 추출
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            empId = ((EmpDetails) authentication.getPrincipal()).getEmpId();
        }

        DetailsLeaveAbsence detailsLeaveAbsence = apiDetailsLeaveAbsenceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "존재하지 않는 휴가신청서입니다."));

        if(Objects.equals(detailsLeaveAbsence.getProcessingStatus(), "접수") || Objects.equals(detailsLeaveAbsence.getProcessingStatus(), "승인")){
            throw new AppException(ErrorCode.INVALID_OPERATION, "이미 처리된 요청입니다.");
        }

        //인증정보를 바탕으로 empId확인 후 없다면 로그인페이지로 이동
        Employee employee = apiEmpLoginRepository.findById(empId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,"  로그인 내역이 존재하지 않습니다."));

        detailsLeaveAbsence.setReceptionAdmin(employee);
        detailsLeaveAbsence.setReceptionDate(LocalDateTime.now());
        detailsLeaveAbsence.setAdminComment(request.getAdminComment());
        detailsLeaveAbsence.setProcessingStatus("접수");

        apiDetailsLeaveAbsenceRepository.save(detailsLeaveAbsence);
    }

    @Transactional
    public void offProcess(Long id, ReceptRequest request) {

        //authentication객체에 SecurityContextHolder를 담아서 인증정보를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //authentication에서 empId 추출
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            empId = ((EmpDetails) authentication.getPrincipal()).getEmpId();
        }

        DetailsLeaveAbsence detailsLeaveAbsence = apiDetailsLeaveAbsenceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "존재하지 않는 휴가신청서입니다."));

        if(Objects.equals(detailsLeaveAbsence.getProcessingStatus(), "승인")){
            throw new AppException(ErrorCode.INVALID_OPERATION, "이미 처리된 요청입니다.");
        }

        //인증정보를 바탕으로 empId확인 후 없다면 로그인페이지로 이동
        Employee employee = apiEmpLoginRepository.findById(empId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,"  로그인 내역이 존재하지 않습니다."));

        detailsLeaveAbsence.setProcessedAdmin(employee);
        detailsLeaveAbsence.setProcessedDate(LocalDateTime.now());
        detailsLeaveAbsence.setAdminComment(request.getAdminComment());
        detailsLeaveAbsence.setProcessingStatus("승인");

        apiDetailsLeaveAbsenceRepository.save(detailsLeaveAbsence);

        // 연차 사용 후 남은 연차 수 갱신
        EmployeeProfile employeeProfile = detailsLeaveAbsence.getEmployee().getEmployeeProfile();
        long newAnnualQuantity = employeeProfile.getAnnualQuantity() - detailsLeaveAbsence.getAbsenceLeavePeriod();
        employeeProfile.setAnnualQuantity(newAnnualQuantity);

        // 변경 사항 저장
        apiDetailsLeaveAbsenceRepository.save(detailsLeaveAbsence);
        employeeProfileRepository.save(employeeProfile);
    }

}
