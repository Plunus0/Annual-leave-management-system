package com.the_daul_intra.mini.controller;


import com.the_daul_intra.mini.dto.entity.DetailsLeaveAbsence;
import com.the_daul_intra.mini.dto.request.*;
import com.the_daul_intra.mini.dto.response.*;
import com.the_daul_intra.mini.service.ApiEmpService;
import com.the_daul_intra.mini.service.ApiLeaveService;
import com.the_daul_intra.mini.service.ApiNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private final ApiEmpService apiEmpService;
    private final ApiLeaveService apiLeaveService;
    private final ApiNoticeService apiNoticeService;

    //로그인 컨트롤러
    @PostMapping("/login")
    public ResponseEntity<ApiLoginResponse> login(@RequestBody ApiLoginPostRequest request) {
        return ResponseEntity.ok(apiEmpService.apiLogin(request));
    }

    @GetMapping("/login")
    public ResponseEntity<ApiLoginResponse> login() {
        return ResponseEntity.ok(apiEmpService.apiGetLogin());
    }

    //로그인 요청 -> 토큰 반환
    //가지고 있는 토큰을 요청할때마다 보내줌 >

    //모바일 처음 기동시 토큰 유효성 검사.......
    //get으로 요청을 보냈는데 > 토큰을 검사했을때 ㅇㅋ이면 넘어가고 아니면 에러로 다시 로그인페이지로

    //휴가신청서 작성 컨트롤러
    @PostMapping("/offwrite")
    public ResponseEntity<DetailsLeaveAbsence> createLeaveRequest(@RequestBody ApiLeavePostRequest request) {
        DetailsLeaveAbsence leaveRequest = apiLeaveService.createLeaveRequest(request);
        return ResponseEntity.ok(leaveRequest);
    }

    //휴가신청서 리스트
    @GetMapping("/off")
    public ResponseEntity<List<ApiOffListItemResponse>> searchLeaveListRequests(@ModelAttribute ApiLeaveSearchRequest request) {
        List<ApiOffListItemResponse> response = apiLeaveService.searchLeavesList(request);
        return ResponseEntity.ok(response);
    }

    //휴가신청서 상세
    @GetMapping("/offdetail")
    public ResponseEntity<ApiOffDetailResponse> getLeaveDetail(@RequestBody ApiOffDetailRequest request) {
        ApiOffDetailResponse response = apiLeaveService.getLeaveDetails(request.getId());
        return ResponseEntity.ok(response);
    }

    //공지사항 목록
    @GetMapping("/notice")
    public ResponseEntity<List<ApiNoticeListItemResponse>> getAllNotices() {
        List<ApiNoticeListItemResponse> notices = apiNoticeService.getNoticeList();
        return ResponseEntity.ok(notices);
    }
    //공지사항 상세
//    @GetMapping("/notice/{id}")
    @GetMapping("/noticedetail")
    public ResponseEntity<ApiNoticeResponse> getNotice(@RequestBody ApiNoticeDetailRequest request) {
        ApiNoticeResponse response = apiNoticeService.getNoticeDetails(request);
        return ResponseEntity.ok(response);
    }

}

