package com.uk02.rmw.dtos;

public record PasswordChangeDTO (
    String oldPassword,
    String newPassword
) { }
