package com.uk02.rmw.services;

import com.uk02.rmw.dtos.authentication.UserResponseDTO;
import com.uk02.rmw.models.User;
import com.uk02.rmw.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use.");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already taken.");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        return user;
    }

    public void changePassword(User user, String oldPassword, String newPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalStateException("Incorrect old password.");
        }

        if (oldPassword.equals(newPassword)) {
            throw new IllegalStateException("New password cannot be the same as the old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public UserResponseDTO getUserDetails(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
