package com.the_daul_intra.mini.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String token;
    private String name;
    private Long id;

}