package com.the_daul_intra.mini.dto.request;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.Notice;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Data
public class NoticeWriteDTO {

    private String title;
    private String content;
    private Employee employee;
    @Builder
    public NoticeWriteDTO(String title, String content, Employee employee) {
        this.title = title;
        this.content = content;
        this.employee = employee;
    }

    public Notice build(){

        return Notice.builder()
                .title(this.title)
                .content(this.content)
                .employee(this.employee)
                .build();

    }

}
