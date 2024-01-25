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
        System.out.println("loginController");
        return ResponseEntity.ok(apiEmpService.apiGetLogin());
    }

    //휴가신청서 작성 컨트롤러
    @PostMapping("/offwrite")
    public ResponseEntity<DetailsLeaveAbsence> createLeaveRequest(@RequestBody ApiLeavePostRequest request) {
        System.out.println("offwriteController");
        DetailsLeaveAbsence leaveRequest = apiLeaveService.createLeaveRequest(request);
        return ResponseEntity.ok(leaveRequest);
    }

    //휴가신청서 리스트
    @GetMapping("/off")
    public ResponseEntity<List<ApiOffListItemResponse>> searchLeaveListRequests(@ModelAttribute ApiLeaveSearchRequest request) {
        System.out.println("offController");
        List<ApiOffListItemResponse> response = apiLeaveService.searchLeavesList(request);
        return ResponseEntity.ok(response);
    }

    //휴가신청서 상세
    @GetMapping("/offdetail")
    public ResponseEntity<ApiOffDetailResponse> getLeaveDetail(@RequestBody ApiOffDetailRequest request) {
        System.out.println("offdetailController");
        ApiOffDetailResponse response = apiLeaveService.getLeaveDetails(request.getId());
        return ResponseEntity.ok(response);
    }

    //공지사항 목록
    @GetMapping("/notice")
    public ResponseEntity<List<ApiNoticeListItemResponse>> getAllNotices() {
        System.out.println("noticeController");
        List<ApiNoticeListItemResponse> notices = apiNoticeService.getNoticeList();
        return ResponseEntity.ok(notices);
    }
    //공지사항 상세
    @GetMapping("/noticedetail")
    public ResponseEntity<ApiNoticeResponse> getNotice(@RequestBody ApiNoticeDetailRequest request) {
        System.out.println("noticedetailController");
        ApiNoticeResponse response = apiNoticeService.getNoticeDetails(request);
        return ResponseEntity.ok(response);
    }

}
