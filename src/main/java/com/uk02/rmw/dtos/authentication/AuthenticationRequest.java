package com.uk02.rmw.dtos.authentication;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
