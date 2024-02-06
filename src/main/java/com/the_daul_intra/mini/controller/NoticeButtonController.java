package com.the_daul_intra.mini.controller;


import com.the_daul_intra.mini.dto.response.ViewerResponse;
import com.the_daul_intra.mini.repository.NoticeRepository;
import com.the_daul_intra.mini.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class NoticeButtonController {
    private final NoticeService noticeService;

    @GetMapping("/notice/{id}/viewers")
    public List<ViewerResponse> getViewers(@PathVariable Long id) {
        return noticeService.getViewersByNoticeId(id);
    }

}
