package com.the_daul_intra.mini.service;

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

    public List<ApiNoticeListItemResponse> getNoticeList() {
        List<Notice> notices = apiNoticeRepository.findAll();
        //추후 인증정보를 바탕으로 로그인한 직원 id확인
        Long empId = 1L;

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
    public ApiNoticeResponse getNoticeDetails(ApiNoticeDetailRequest request) {
        Notice notice = apiNoticeRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND," 해당 공지사항이 존재하지 않습니다."));

        //추후 인증정보를 바탕으로 로그인한 직원 id 확인 -> 없다면 로그인 페이지
        Long empId = 1L;

        Employee employee = apiEmpLoginRepository.findById(empId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, " 로그인 내역이 존재하지 않습니다."));

        NoticeReadStatus readStatus = apiNoticeReadStatusRepository.findByNoticeIdAndEmployeeId(request.getId(), empId)
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
