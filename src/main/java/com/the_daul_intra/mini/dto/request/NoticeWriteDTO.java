package com.the_daul_intra.mini.dto.request;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.Notice;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
@Data
public class NoticeWriteDTO {

    private Long id;
    private String title;
    private String content;
    private Employee employee;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime regDate;
    // 공지사항 list 날짜 및 번호
    private String onlyDate;
    private int sortNum;
    @Builder
    public NoticeWriteDTO(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.employee = notice.getEmployee();
        this.regDate = notice.getRegDate();
    }

    public Notice build(){



        return Notice.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .employee(this.employee)
                .regDate(this.regDate)
                .build();

    }


}
