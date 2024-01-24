package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.dto.request.NoticeWriteDTO;
import com.the_daul_intra.mini.dto.response.NoticeResponse;
import com.the_daul_intra.mini.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
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
        
        // 테스트용 임시로 employee id 만들어서 사용
        Employee employee = Employee.builder()
                .id(1L)
                .build();
        
        noticeWriteDTO.setEmployee(employee);
        // 여기까지

        noticeService.noticeWrite(noticeWriteDTO);

        return "redirect:/admin/notice/list";
    }

    // 공지사항 상세페이지
    @GetMapping("/notice/detail/{id}")
    public String noticeDetail(@PathVariable Long id, Model model){


        Notice noticeDetail = noticeService.noticeDetail(id);

        model.addAttribute("noticeDetail", noticeDetail);

        return "noticeDetail";
    }

    // 공지사항 수정페이지
    @PutMapping("/notice/modify/{id}")
    public String noticeModify(@PathVariable Long id, @ModelAttribute NoticeResponse noticeResponse){

        // 테스트용 임시로 employee id 만들어서 사용
        Employee employee = Employee.builder()
                .id(1L)
                .build();

        noticeResponse.setEmployee(employee);
        // 여기까지

        System.out.println("id : " + id);
        noticeService.noticeModify(id, noticeResponse);

        return "redirect:/admin/notice/detail/{id}";
    }

    @DeleteMapping("/notice/delete/{id}")
    public String noticeDelete(@PathVariable Long id){
        System.out.println("Delete id : " + id);
        noticeService.noticeDelete(id);
        return "redirect:/admin/notice/list";
    }

    //member
    private NoticeService noticeService;

}
