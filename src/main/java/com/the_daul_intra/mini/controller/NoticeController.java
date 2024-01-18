package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class NoticeController {

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/notice/list")
    public String noticeListView(Model model){

        List<Notice> list = noticeService.noticeList();

        model.addAttribute("list", list);

        return "noticeList";
    }

    @GetMapping("/notice/test")
    public String noticeListTest(@RequestParam String data, Model model){

        System.out.println("test data 수신 : " + data);


        return "";
    }


    //member
    private NoticeService noticeService;

}
