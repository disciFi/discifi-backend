package com.uk02.rmw.dtos.authentication;

public record PasswordChangeDTO (
    String oldPassword,
    String newPassword
) { }
