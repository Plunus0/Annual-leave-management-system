package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.response.OffDetailResponse;
import com.the_daul_intra.mini.dto.response.OffListResponse;
import com.the_daul_intra.mini.exception.AppException;
import com.the_daul_intra.mini.exception.ErrorCode;
import com.the_daul_intra.mini.service.OffService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class OffController {

    private final OffService offService;

/*    @GetMapping("/off")
    public String offSerchList(@RequestParam(required = false) String absenceType,
                               @RequestParam(required = false) String status,
                               Model model) {
        List<OffListResponse> offSerchList = offService.getOffSerchList(absenceType, status);
        model.addAttribute("offSerchList", offSerchList);
        return "offRequestList";
    }*/
    @GetMapping("/off")
    public String offSearchList(
            @RequestParam(value = "page", defaultValue = "1") @Min(value = 1, message = "최소 페이지는 1페이지 입니다.") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(required = false) String absenceType,
            @RequestParam(required = false) String status,
            Model model) {
        Page<OffListResponse> offSearchList = offService.getOffSearchList(page, size, absenceType, status);
        model.addAttribute("offSearchList", offSearchList);
        return "offRequestList";
    }

    @GetMapping("/off/{id}")
    public String offDetail(@PathVariable Long id, Model model) {
        OffDetailResponse detailResponse = offService.getOffDetail(id);
        model.addAttribute("detail", detailResponse);
        return "offDetail";
    }

    @PostMapping("/off/delete/{id}")
    public String deleteOff(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            offService.deleteLeaveAbsence(id);
            redirectAttributes.addFlashAttribute("successMessage", "신청서가 성공적으로 삭제되었습니다.");
            return "redirect:/admin/off";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "신청서 삭제 중 문제가 발생했습니다.");
            return "redirect:/admin/off";
        }
    }
}
