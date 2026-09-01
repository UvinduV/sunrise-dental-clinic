package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.request.LoginRequest;
import com.sunrise.dentalclinic.dto.response.LoginResponse;
import com.sunrise.dentalclinic.exception.InvalidCredentialsException;
import org.springframework.stereotype.Service;


// TODO : replace with auth with JWT in future

@Service
public class AuthService {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "1234";

    public LoginResponse login(LoginRequest request) {
        if (!ADMIN_USERNAME.equals(request.getUsername()) || !ADMIN_PASSWORD.equals(request.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return new LoginResponse(ADMIN_USERNAME, "ADMIN", "Login successful");
    }
}
