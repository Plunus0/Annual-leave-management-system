package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.entity.DetailsLeaveAbsence;
import com.the_daul_intra.mini.dto.response.OffListResponse;
import com.the_daul_intra.mini.repository.ApiDetailsLeaveAbsenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OffService {

    private final ApiDetailsLeaveAbsenceRepository apiDetailsLeaveAbsenceRepository;

    public List<OffListResponse> getOffSerchList(String absenceType, String status) {
        Specification<DetailsLeaveAbsence> spec = LeaveSpecifications.withAdminCriteria(absenceType, status);

        return apiDetailsLeaveAbsenceRepository.findAll(spec).stream()
                .map(application -> new OffListResponse(
                        application.getId(),
                        application.getEmployee().getEmployeeProfile().getName(),
                        application.getEmployee().getEmployeeProfile().getContactInformation(),
                        application.getApplicationDate().toString(),
                        application.getAbsenceType(),
                        application.getApplicantComments(),
                        application.getProcessingStatus()
                )).collect(Collectors.toList());
    }



}
