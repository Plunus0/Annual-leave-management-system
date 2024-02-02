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

    @DeleteMapping("/off/{id}")
    public ResponseEntity<?> offDelete(@PathVariable Long id) {
        try {
            offService.deleteLeaveAbsence(id);
            return ResponseEntity.ok().build();
        } catch (AppException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new AppException(ErrorCode.NOT_FOUND," 신청서 삭제 중 문제가 발생했습니다."));
        }
    }

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
