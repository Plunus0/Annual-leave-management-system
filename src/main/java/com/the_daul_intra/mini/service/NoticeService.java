package com.the_daul_intra.mini.service;


import com.the_daul_intra.mini.dto.entity.Notice;
import com.the_daul_intra.mini.dto.request.NoticeWriteDTO;
import com.the_daul_intra.mini.dto.response.NoticeResponse;
import com.the_daul_intra.mini.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // r
    public List<NoticeWriteDTO> noticeList(){

        List<NoticeWriteDTO> noticeWriteDTOS = new ArrayList<>();

        List<Notice> notices = noticeRepository.findAll();
        for (Notice notice : notices) {

            NoticeWriteDTO temp = new NoticeWriteDTO(notice);
            noticeWriteDTOS.add(temp);

        }

        return noticeWriteDTOS;
    }

    public Notice noticeWrite(NoticeWriteDTO noticeWriteDTO){

        return noticeRepository.save(noticeWriteDTO.build());
    }

    public Notice noticeDetail(Long id){
        try {
            Notice notice = noticeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Notice not found for ID: " + id));

            return notice;
        } catch (Exception e) {
            // 더 자세한 예외 정보를 출력
            e.printStackTrace();
            throw e; // 예외를 다시 던져서 컨트롤러에서도 예외 정보를 확인할 수 있도록 함
        }
    }
    @Transactional
    public void noticeModify(Long id, NoticeResponse noticeResponse){

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("update error"));

        //noticeResponse.setUpdateDate();


        notice.setTitle(noticeResponse.getTitle());
        notice.setContent(noticeResponse.getContent());

    }

    @Transactional
    public void noticeDelete(Long id){

        System.out.println("Delete Service id : " + id);
        noticeRepository.delete(noticeRepository
                .findById(id).orElseThrow(()-> new IllegalArgumentException("Can't find id : " + id)));

    }

    //member
    private NoticeRepository noticeRepository;

}
