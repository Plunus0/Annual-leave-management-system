package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.request.ReceptRequest;
import com.the_daul_intra.mini.exception.AppException;
import com.the_daul_intra.mini.exception.ErrorCode;
import com.the_daul_intra.mini.service.OffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class OffButtonController {
    private final OffService offService;

    @PutMapping("/off/{id}/receipt")
    public ResponseEntity<?> offReceipt(@PathVariable Long id, @RequestBody ReceptRequest request) {
        offService.offRecept(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/off/{id}/process")
    public ResponseEntity<?> offProcess(@PathVariable Long id, @RequestBody ReceptRequest request) {
        offService.offProcess(id, request);
        return ResponseEntity.ok().build();
    }
}
