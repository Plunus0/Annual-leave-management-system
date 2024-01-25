package com.the_daul_intra.mini.service;

import com.the_daul_intra.mini.dto.EmpDetails;
import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.dto.entity.NoticeReadStatus;
import com.the_daul_intra.mini.dto.entity.YesNo;
import com.the_daul_intra.mini.dto.request.ApiNoticeDetailRequest;
import com.the_daul_intra.mini.dto.response.ApiNoticeListItemResponse;
import com.the_daul_intra.mini.dto.response.ApiNoticeResponse;
import com.the_daul_intra.mini.exception.AppException;
import com.the_daul_intra.mini.exception.ErrorCode;
import com.the_daul_intra.mini.repository.ApiEmpLoginRepository;
import com.the_daul_intra.mini.repository.ApiNoticeReadStatusRepository;
import com.the_daul_intra.mini.repository.ApiNoticeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiNoticeService {
    private final ApiNoticeRepository apiNoticeRepository;
    private final ApiNoticeReadStatusRepository apiNoticeReadStatusRepository;
    private final ApiEmpLoginRepository apiEmpLoginRepository;

    Long empId = null;
    public List<ApiNoticeListItemResponse> getNoticeList() {
        List<Notice> notices = apiNoticeRepository.findAll();

        //authentication객체에 SecurityContextHolder를 담아서 인증정보를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //authentication에서 empId 추출
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            empId = ((EmpDetails) authentication.getPrincipal()).getEmpId();
        }

        return notices.stream().map(notice -> {
            boolean isRead = apiNoticeReadStatusRepository.findByNoticeIdAndEmployeeId(notice.getId(), empId)
                    .map(readStatus -> readStatus.getIsRead() == YesNo.Y)
                    .orElse(false);

            return new ApiNoticeListItemResponse(
                    notice.getId(),
                    notice.getTitle(),
                    notice.getRegDate().toString(),
                    isRead
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public ApiNoticeResponse getNoticeDetails(Long requestId) {
        Notice notice = apiNoticeRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND," 해당 공지사항이 존재하지 않습니다."));

        //authentication객체에 SecurityContextHolder를 담아서 인증정보를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //authentication에서 empId 추출
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            empId = ((EmpDetails) authentication.getPrincipal()).getEmpId();
        }

        Employee employee = apiEmpLoginRepository.findById(empId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, " 로그인 내역이 존재하지 않습니다."));

        NoticeReadStatus readStatus = apiNoticeReadStatusRepository.findByNoticeIdAndEmployeeId(requestId, empId)
                .orElseGet(() -> {
                    return apiNoticeReadStatusRepository.save(NoticeReadStatus.builder()
                            .notice(notice)
                            .employee(employee)
                            .readDate(LocalDateTime.now())
                            .isRead(YesNo.Y)
                            .build());
                });

        readStatus.setReadDate(LocalDateTime.now());
        apiNoticeReadStatusRepository.save(readStatus);

        return ApiNoticeResponse.builder()
                .id(notice.getId())
                .adminName(notice.getEmployee().getEmployeeProfile().getName())
                .title(notice.getTitle())
                .content(notice.getContent())
                .build();
    }


}
