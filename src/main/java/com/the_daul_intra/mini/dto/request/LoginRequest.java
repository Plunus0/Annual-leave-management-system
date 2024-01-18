package com.the_daul_intra.mini.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginRequest {

        private String email;
        private String password;

}