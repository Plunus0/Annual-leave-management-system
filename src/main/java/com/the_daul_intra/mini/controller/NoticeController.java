package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.dto.request.NoticeWriteDTO;
import com.the_daul_intra.mini.dto.response.NoticeResponse;
import com.the_daul_intra.mini.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static java.util.Comparator.comparing;

@Controller
@RequestMapping("/admin/notice")
public class NoticeController {

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    //전체 공지사항 목록
    @GetMapping("/")
    public String noticeListView(Model model){

        List<NoticeWriteDTO> list = noticeService.noticeList();

        list.sort(comparing(NoticeWriteDTO::getRegDate).reversed());

        int count = 0;
        for(NoticeWriteDTO iter : list){

            LocalDateTime localDateTime = list.get(count).getRegDate();
            list.get(count).setOnlyDate(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            list.get(count).setSortNum(++count);
        }

        model.addAttribute("list", list);
        return "noticeList";
    }

    //글 작성 페이지
    @GetMapping("/write")
    public String noticeWrite(Model model){

        model.addAttribute("write", new NoticeWriteDTO());

        return "noticeWrite";
    }

    //글 작성
    @PostMapping("/writepro")
    public String noticeWritepro(@ModelAttribute  NoticeWriteDTO noticeWriteDTO) {
        
        // 테스트용 임시로 employee id 만들어서 사용
        Employee employee = Employee.builder()
                .id(1L)
                .build();

        noticeWriteDTO.setEmployee(employee);
        // 여기까지

        noticeService.noticeWrite(noticeWriteDTO);

        return "redirect:/admin/notice/";
    }

    // 공지사항 상세페이지
    @GetMapping("/{id}")
    public String noticeDetail(@PathVariable Long id, Model model){


        Notice noticeDetail = noticeService.noticeDetail(id);

        model.addAttribute("noticeDetail", noticeDetail);

        return "noticeDetail";
    }

    // 공지사항 수정페이지
    /*@PutMapping("/modify/{id}")
    public String noticeModify(@PathVariable Long id, @ModelAttribute NoticeResponse noticeResponse){
        // 테스트용 임시로 employee id 만들어서 사용
        Employee employee = Employee.builder()
                .id(1L)
                .build();

        noticeResponse.setEmployee(employee);
        // 여기까지
        noticeService.noticeModify(id, noticeResponse);

        return "redirect:/admin/notice/{id}";
    }*/

    @GetMapping("/modify/{id}")
    public String noticeModify(@PathVariable Long id, Model model){
        // 테스트용 임시로 employee id 만들어서 사용
        Employee employee = Employee.builder()
                .id(1L)
                .build();

        // 여기까지

        Notice notice = noticeService.noticeDetail(id);

        model.addAttribute("notice", notice);

        return "noticeModify";
    }

    @PutMapping("/modifyPro/{id}")
    public String noticeModifyPro(@PathVariable Long id, @ModelAttribute NoticeResponse noticeResponse){
        // 테스트용 임시로 employee id 만들어서 사용
        Employee employee = Employee.builder()
                .id(1L)
                .build();

        noticeResponse.setEmployee(employee);
        // 여기까지
        noticeService.noticeModify(id, noticeResponse);

        return "redirect:/admin/notice/{id}";
    }


    @DeleteMapping("/delete/{id}")
    public String noticeDelete(@PathVariable Long id){

        noticeService.noticeDelete(id);
        return "redirect:/admin/notice/";
    }

    //member
    private NoticeService noticeService;

}
