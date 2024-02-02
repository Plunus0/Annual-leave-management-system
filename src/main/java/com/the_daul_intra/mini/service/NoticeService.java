package com.the_daul_intra.mini.service;


import com.the_daul_intra.mini.dto.EmpDetails;
import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.dto.entity.NoticeReadStatus;
import com.the_daul_intra.mini.dto.entity.YesNo;
import com.the_daul_intra.mini.dto.request.NoticePostRequest;
import com.the_daul_intra.mini.dto.response.NoticeDetailResponse;
import com.the_daul_intra.mini.dto.response.NoticeListResponse;
import com.the_daul_intra.mini.dto.response.NoticeResponse;
import com.the_daul_intra.mini.dto.response.ViewerResponse;
import com.the_daul_intra.mini.exception.AppException;
import com.the_daul_intra.mini.exception.ErrorCode;
import com.the_daul_intra.mini.repository.ApiNoticeReadStatusRepository;
import com.the_daul_intra.mini.repository.EmployeeRepository;
import com.the_daul_intra.mini.repository.NoticeRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final EmployeeRepository employeeRepository;
    private final ApiNoticeReadStatusRepository apiNoticeReadStatusRepository;

    Long empId = null;
    Employee employee = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    //게시글 작성
    public Long noticeWrite(NoticePostRequest noticePostRequest){
        //authentication객체에 SecurityContextHolder를 담아서 인증정보를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //authentication에서 empId 추출
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            empId = ((EmpDetails) authentication.getPrincipal()).getEmpId();
            employee = employeeRepository.findById(empId)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "로그인 이력이 없습니다. 로그인 페이지로 돌아갑니다."));
        }

        Notice notice = Notice.builder()
                .title(noticePostRequest.getTitle())
                .content(noticePostRequest.getContent())
                .employee(employee)
                .build();

        notice = noticeRepository.save(notice);

        return notice.getId();
    }

    //게시글 페이징 리스트
    public Page<NoticeListResponse> getNoticePagingList(int page, int size, String delete) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("regDate").descending());

        // 'delete' 값에 따른 Specification 조건 생성
        Specification<Notice> spec = (root, query, criteriaBuilder) -> {
            if ("Y".equals(delete)) {
                return criteriaBuilder.equal(root.get("unused"), YesNo.Y);
            } else {
                // 'delete'가 "N"이거나 null인 경우
                return criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("unused"), YesNo.N),
                        criteriaBuilder.isNull(root.get("unused"))
                );
            }
        };

        // 공지사항 조회
        Page<Notice> notices = noticeRepository.findAll(spec, pageable);

        // 전체 공지사항 수
        long totalNotices = notices.getTotalElements();
        // 현재 페이지의 첫 번째 공지사항 번호
        AtomicInteger startNumber = new AtomicInteger((int) totalNotices - (page - 1) * size);

        return notices.map(notice -> {
            String writeId = notice.getEmployee() != null ? notice.getEmployee().getEmployeeProfile().getName() : null;
            String regDateStr = notice.getRegDate() != null ? notice.getRegDate().format(formatter) : null;
            // NoticeReadStatus 테이블을 이용하여 조회수 계산
            Long viewCount = apiNoticeReadStatusRepository.countByNoticeAndIsRead(notice, YesNo.Y);

            return new NoticeListResponse(
                    notice.getId(),
                    (long) startNumber.getAndDecrement(),
                    notice.getTitle(),
                    regDateStr,
                    writeId,
                    viewCount
            );
        });
    }

    public List<ViewerResponse> getViewersByNoticeId(Long noticeId) {
        List<NoticeReadStatus> readStatuses = apiNoticeReadStatusRepository.findByNoticeId(noticeId);
        return readStatuses.stream()
                .map(readStatus -> new ViewerResponse(
                        readStatus.getEmployee().getId(),
                        readStatus.getEmployee().getEmployeeProfile().getName(),
                        readStatus.getEmployee().getEmployeeProfile().getContactInformation()))
                .collect(Collectors.toList());
    }





    public NoticeDetailResponse noticeDetail(Long id) {
        // ID를 사용하여 Notice 엔티티 조회
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "해당하는 공지사항이 없습니다."));

        Long viewCount = apiNoticeReadStatusRepository.countByNoticeAndIsRead(notice, YesNo.Y);

        // 조회된 Notice 엔티티로부터 정보 추출 및 NoticeDetailResponse 객체 생성
        return NoticeDetailResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .regDate(notice.getRegDate().format(formatter))
                .writeId(notice.getEmployee().getEmployeeProfile().getName())
                .viewCount(viewCount)
                .build();
    }

    @Transactional
    public void noticeModify(Long id, NoticeResponse noticeResponse){

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("update error"));

        //noticeResponse.setUpdateDate();


        notice.setTitle(noticeResponse.getTitle());
        notice.setContent(noticeResponse.getContent());

    }

    @Transactional
    public void noticeDelete(Long id){

        System.out.println("Delete Service id : " + id);
        noticeRepository.delete(noticeRepository
                .findById(id).orElseThrow(()-> new IllegalArgumentException("Can't find id : " + id)));

    }

}
