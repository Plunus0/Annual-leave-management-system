package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.dto.request.NoticeWriteDTO;
import com.the_daul_intra.mini.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class NoticeController {

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    //전체 공지사항 목록
    @GetMapping("/notice/list")
    public String noticeListView(Model model){

        List<Notice> list = noticeService.noticeList();

        model.addAttribute("list", list);


        return "noticeList";
    }

    //글 작성 페이지
    @GetMapping("/notice/write")
    public String noticeWrite(Model model){

        model.addAttribute("write", new NoticeWriteDTO());

        return "noticeWrite";
    }

    //글 작성
    @PostMapping("/notice/writepro")
    public String noticeWritepro(@ModelAttribute  NoticeWriteDTO noticeWriteDTO, Model model) {

        System.out.println("진입 체크 지점");

        System.out.println("title : " + noticeWriteDTO.getTitle());
        System.out.println("content : " + noticeWriteDTO.getContent());

        noticeService.noticeWrite(noticeWriteDTO);

        return "redirect:/noticeList";
    }

    // 공지사항 상세페이지
    @GetMapping("/notice/detail/{id}")
    public String noticeDetail(@PathVariable Long id, Model model){
        System.out.println("test");


        Notice noticeDetail = noticeService.noticeDetail(id);

        System.out.println("title : " + noticeDetail.getTitle());
        System.out.println("content : " + noticeDetail.getContent());

        model.addAttribute("noticeDetail", noticeDetail);

        return "noticeDetail";
    }

    //member
    private NoticeService noticeService;

}
