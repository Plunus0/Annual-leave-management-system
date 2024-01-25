package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.entity.DetailsLeaveAbsence;
import com.the_daul_intra.mini.dto.entity.DetailsLeaveDate;
import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.dto.response.OffDetailResponse;
import com.the_daul_intra.mini.dto.response.OffListResponse;
import com.the_daul_intra.mini.exception.AppException;
import com.the_daul_intra.mini.exception.ErrorCode;
import com.the_daul_intra.mini.repository.ApiDetailsLeaveAbsenceRepository;
import com.the_daul_intra.mini.repository.ApiDetailsLeaveDateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OffService {

    private final ApiDetailsLeaveAbsenceRepository apiDetailsLeaveAbsenceRepository;
    private final ApiDetailsLeaveDateRepository apiDetailsLeaveDateRepository;

    DateTimeFormatter formatter;
    public List<OffListResponse> getOffSerchList(String absenceType, String status) {
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
    }

    public OffDetailResponse getOffDetail(Long id) {
        DetailsLeaveAbsence leaveAbsence = apiDetailsLeaveAbsenceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND," 해당 신청서가 존재하지 않습니다."));

        formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");

        Employee employee = leaveAbsence.getEmployee();
        EmployeeProfile employeeProfile = employee.getEmployeeProfile();
        Employee processedAdmin = leaveAbsence.getProcessedAdmin();
        EmployeeProfile processedAdminProfile = processedAdmin.getEmployeeProfile();

        LocalDate[] useDates = leaveAbsence.getLeaveDates().stream()
                .map(DetailsLeaveDate::getUseDate)
                .toArray(LocalDate[]::new);

        return OffDetailResponse.builder()
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
                .receiveAdmin(processedAdminProfile.getName() != null ? processedAdminProfile.getName() : null)
                .confirmAdmin(processedAdminProfile.getName() != null ? processedAdminProfile.getName() : null)
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

}
