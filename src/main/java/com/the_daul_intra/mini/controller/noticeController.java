package com.the_daul_intra.mini.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class noticeController {

    @GetMapping("/notice/list")
    public String noticeListView(){


        return "noticeList";
    }

    @GetMapping("/notice/test/{id}")
    public String noticeListTest(@PathVariable int id){

        System.out.println("데이터 송수신 : " + id);

        return "";
    }
}
