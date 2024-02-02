package com.the_daul_intra.mini.configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptEncoder {
    public static String encodePassword(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(rawPassword);
    }
}
