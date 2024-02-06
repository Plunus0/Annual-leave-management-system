package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.dto.request.NoticePostRequest;
import com.the_daul_intra.mini.dto.response.ApiNoticeListResponse;
import com.the_daul_intra.mini.dto.response.NoticeDetailResponse;
import com.the_daul_intra.mini.dto.response.NoticeListResponse;
import com.the_daul_intra.mini.dto.response.NoticeResponse;
import com.the_daul_intra.mini.service.NoticeService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static java.util.Comparator.comparing;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class NoticeController {

    private final NoticeService noticeService;

    //공지사항 작성페이지
    @GetMapping("/notice/write")
    public String noticeWrite(){
        return "noticeWrite";
    }

    //공지사항 작성요청
    @PostMapping("/notice/write")
    public String noticeWritePost(@ModelAttribute NoticePostRequest noticePostRequest) {
        Long id = noticeService.noticeWrite(noticePostRequest);
        return "redirect:/admin/notice/" + id;
    }

    //전체 공지사항 목록
    @GetMapping("/notice")
    public String noticePagingList(
            @RequestParam(value = "page", defaultValue = "1") @Min(value = 1, message = "최소 페이지는 1페이지 입니다.") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "delete", defaultValue = "N") String delete,
            Model model){
        Page<NoticeListResponse> noticeList = noticeService.getNoticePagingList(page, size, delete);
        model.addAttribute("noticeList", noticeList);
        return "noticeList";
    }

    //공지사항 상세페이지
    @GetMapping("/notice/{id}")
    public String noticeDetail(@PathVariable Long id, Model model){

        NoticeDetailResponse noticeDetail = noticeService.noticeDetail(id);

        model.addAttribute("noticeDetail", noticeDetail);

        return "noticeDetail";
    }

    //공지사항 수정페이지
    @GetMapping("/notice/modify/{id}")
    public String noticeModify(@PathVariable Long id, Model model){
        NoticeDetailResponse notice = noticeService.noticeDetail(id);
        model.addAttribute("notice", notice);
        return "noticeModify";
    }

    //공지사항 수정요청
    @PutMapping("/notice/modify/{id}")
    public String noticeModifyPro(@PathVariable Long id, @ModelAttribute NoticeResponse noticeResponse){
        noticeService.noticeModify(id, noticeResponse);
        return "redirect:/admin/notice/{id}";
    }


    //공지사항 삭제요청
    @PostMapping("/notice/delete/{id}")
    public String noticeDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            noticeService.noticeDelete(id);
            redirectAttributes.addFlashAttribute("successMessage", "성공적으로 삭제되었습니다.");
            return "redirect:/admin/notice";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제 중 문제가 발생했습니다.");
            return "redirect:/admin/notice";
        }
    }

}
