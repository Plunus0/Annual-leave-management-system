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

    //휴가신청서 작성 컨트롤러
    @PostMapping("/off")
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
    @GetMapping("/off/{id}")
    public ResponseEntity<ApiOffDetailResponse> getLeaveDetail(@PathVariable Long id) {
        ApiOffDetailResponse response = apiLeaveService.getLeaveDetails(id);
        return ResponseEntity.ok(response);
    }

    //공지사항 목록
    @GetMapping("/notice")
    public ResponseEntity<List<ApiNoticeListItemResponse>> getAllNotices() {
        List<ApiNoticeListItemResponse> notices = apiNoticeService.getNoticeList();
        return ResponseEntity.ok(notices);
    }
    //공지사항 상세
    @GetMapping("/notice/{id}")
    public ResponseEntity<ApiNoticeResponse> getNotice(@PathVariable Long id) {
        ApiNoticeResponse response = apiNoticeService.getNoticeDetails(id);
        return ResponseEntity.ok(response);
    }

}
