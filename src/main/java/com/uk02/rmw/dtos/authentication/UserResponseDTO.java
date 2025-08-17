package com.uk02.rmw.dtos.authentication;

public record UserResponseDTO (
    Long id,
    String username,
    String email
) { }
