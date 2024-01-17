package com.the_daul_intra.mini.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class noticeController {

    @GetMapping("/notice/list")
    public String noticeListView(Model model){
        String message = "통신이 되나 안되나 테스트";

        model.addAttribute("message", message);

        return "noticeList";
    }

    @GetMapping("/notice/test")
    public String noticeListTest(@RequestParam String data, Model model){

        System.out.println("test data 수신 : " + data);


        return "";
    }


}
