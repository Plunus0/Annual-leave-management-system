package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.response.OffListResponse;
import com.the_daul_intra.mini.service.OffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class OffController {

    private final OffService offService;

    @GetMapping("/off")
    public String offSerchList(@RequestParam(required = false) String absenceType,
                               @RequestParam(required = false) String status,
                               Model model) {
        List<OffListResponse> offSerchList = offService.getOffSerchList(absenceType, status);
        model.addAttribute("offSerchList", offSerchList);
        return "/vacationRequestList";
    }

    @GetMapping("/off/{id}")
    public String offDetail(@PathVariable Long id, Model model) {
        // 상세 정보 로직 (생략)
        return "vacationRequestDetail";
    }
}
