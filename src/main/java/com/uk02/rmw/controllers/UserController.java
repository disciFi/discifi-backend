package com.uk02.rmw.controllers;

import com.uk02.rmw.dtos.PasswordChangeDTO;
import com.uk02.rmw.dtos.UserResponseDTO;
import com.uk02.rmw.models.User;
import com.uk02.rmw.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.ReactiveOffsetScrollPositionHandlerMethodArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getUserDetails(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserDetails(user));
    }

    @PostMapping("/password")
    public ResponseEntity<String> changePassword(
        @Valid @RequestBody PasswordChangeDTO passwordChangeDTO,
        @AuthenticationPrincipal User user
    ) {
        try {
            userService.changePassword(user, passwordChangeDTO.oldPassword(), passwordChangeDTO.newPassword());
            return ResponseEntity.ok().body("Password changed successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
