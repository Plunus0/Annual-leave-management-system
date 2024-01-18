package com.the_daul_intra.mini.controller;


import com.the_daul_intra.mini.dto.request.ApiLoginRequest;
import com.the_daul_intra.mini.dto.response.ApiLoginResponse;
import com.the_daul_intra.mini.service.ApiEmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private final ApiEmpService apiEmpService;
    @PostMapping("/login")
    public ResponseEntity<ApiLoginResponse> login(@RequestBody ApiLoginRequest dto) {
        return ResponseEntity.ok(apiEmpService.apiLogin(dto));
    }

}
