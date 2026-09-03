package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.request.LoginRequest;
import com.sunrise.dentalclinic.dto.request.RegisterRequest;
import com.sunrise.dentalclinic.dto.response.LoginResponse;
import com.sunrise.dentalclinic.entity.User;
import com.sunrise.dentalclinic.exception.InvalidCredentialsException;
import com.sunrise.dentalclinic.exception.UsernameAlreadyExistsException;
import com.sunrise.dentalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.replace("ROLE_", ""))
                .orElse("UNKNOWN");

        return new LoginResponse(authentication.getName(), role, "Login successful");
    }

    public LoginResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(
                    "Username already taken: " + request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().toUpperCase());
        userRepository.save(user);

        return new LoginResponse(user.getUsername(), user.getRole(), "Registration successful");
    }
}
