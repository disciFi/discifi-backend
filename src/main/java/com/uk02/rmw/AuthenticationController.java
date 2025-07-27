package com.uk02.rmw;

import com.uk02.rmw.config.jwt.JwtService;
import com.uk02.rmw.dtos.AuthenticationRequest;
import com.uk02.rmw.dtos.AuthenticationResponse;
import com.uk02.rmw.models.User;
import com.uk02.rmw.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (isAuthenticated()) {
            return new ResponseEntity<>("User is already logged in.", HttpStatus.FORBIDDEN);
        }

        try {
            User registeredUser = userService.registerUser(user);
            registeredUser.setPassword(null);

            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        System.out.println("Login");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.isAuthenticated() &&
                // Ensure the user is not the special 'anonymousUser'
                !authentication.getPrincipal().equals("anonymousUser");
    }
}
