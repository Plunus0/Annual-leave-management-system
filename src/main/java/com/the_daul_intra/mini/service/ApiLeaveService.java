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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

        // 현재 월과 일자
        int currentMonth = LocalDate.now().getMonthValue();
        int currentDay = LocalDate.now().getDayOfMonth();
        System.out.println("currentMonth : " + currentMonth);
        System.out.println("currentDate : " + currentDay);


        // 입사 일자와 월, 일, 현재 잔여 연차 갯수
        LocalDate joinDate = employee.getEmployeeProfile().getJoinDate().toLocalDate();
        int joinMonth = joinDate.getMonthValue();
        int joinDay = joinDate.getDayOfMonth();
        Long offCount = employee.getEmployeeProfile().getAnnualQuantity();
        System.out.println("joinDate : " + joinDate);
        System.out.println("joinMonth : " + joinMonth);
        System.out.println("joinDay : " + joinDay);
        System.out.println("offCount : " + offCount);

        // 연차를 신청한 일 중 가장 빠른 일자와 월, 일, 신청한 연차의 갯수
        LocalDate minDate = Arrays.stream(request.getUseDates()).min(LocalDate::compareTo).orElse(null);
        int minDateMonth = Objects.requireNonNull(minDate).getMonthValue();
        int minDateDay = Objects.requireNonNull(minDate).getDayOfMonth();
        Long requestOffCount = (long) request.getUseDates().length;
        boolean isAnnual = request.getType().equals("연차") || request.getType().equals("반차(오전)") || request.getType().equals("반차(오후)") || request.getType().equals("월차");
        System.out.println("minDate : " + minDate);
        System.out.println("minDateMonth : " + minDateMonth);
        System.out.println("minDateDay : " + minDateDay);
        System.out.println("requestOffCount : " + requestOffCount);
        System.out.println("isAnnual : " + isAnnual);



        System.out.println("ChronoUnit.YEARS.between(joinDate, LocalDate.now()) : " + ChronoUnit.YEARS.between(joinDate, LocalDate.now()));

        // 근속일수가 1년 미만
        if(ChronoUnit.YEARS.between(joinDate, LocalDate.now()) < 1){
            // 입사월과 현재 월일 비교하여 동일월인지 다음월인지 구분
            if(joinMonth == currentMonth)
                offCount++;
        }

        // 신청 갯수보다 사용 가능한 연차가 적을 때
        if (offCount < requestOffCount && isAnnual) {
            throw new AppException(ErrorCode.INVALID_OPERATION, "사용 가능한 연차가 없습니다.");
/*                //연차 사용 불가능한 조건들 열거

            // 1. 근속일수가 1년 이상일 때
            if (ChronoUnit.YEARS.between(joinDate, LocalDate.now()) >= 1) {
                System.out.println("1Y up");
            }
            // 2. 근속일수가 1년 미만이나 입사일 기준 30일 이후의 미발생 연차를 사용하는 경우
            if (!(minDateMonth == currentMonth && minDate.isBefore(LocalDate.now())) &&
                    !(minDateMonth == currentMonth - 1 && minDate.isAfter(LocalDate.now()))) {
                throw new AppException(ErrorCode.INVALID_OPERATION, "사용 가능한 연차가 없습니다. 1Y D ok");
            }else if(employee.getEmployeeProfile().getAnnualQuantity()+1 < request.getUseDates().length){
                throw new AppException(ErrorCode.INVALID_OPERATION, "사용 가능한 연차가 없습니다. 1Y D no");
            }*/
        }

        System.out.println("off insert");
        DetailsLeaveAbsence leaveRequest = DetailsLeaveAbsence.builder()
                .employee(employee)
                .absenceLeavePeriod(requestOffCount)
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
