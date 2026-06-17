package com.rednorte.security.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String rut;
    private String password;
}
