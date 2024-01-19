package com.the_daul_intra.mini.service;


import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.dto.request.NoticeWriteDTO;
import com.the_daul_intra.mini.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // r
    public List<Notice> noticeList(){

        return noticeRepository.findAll();
    }

    public Notice noticeWrite(NoticeWriteDTO noticeWriteDTO){


        return noticeRepository.save(noticeWriteDTO.build());
    }

    public Notice noticeDetail(Long id){

        Notice notice = noticeRepository.findById(id).orElseThrow(NullPointerException::new);

        return notice;
    }

    //member
    private NoticeRepository noticeRepository;
}
