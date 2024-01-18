package com.the_daul_intra.mini.service;


import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // r
    public List<Notice> noticeList(){

        return noticeRepository.findAll();
    }


    //member
    private NoticeRepository noticeRepository;
}
