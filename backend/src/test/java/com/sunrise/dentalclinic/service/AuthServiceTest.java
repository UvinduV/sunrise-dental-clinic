package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.request.LoginRequest;
import com.sunrise.dentalclinic.dto.response.LoginResponse;
import com.sunrise.dentalclinic.exception.InvalidCredentialsException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceTest {

    private final AuthService authService = new AuthService();

    @Test
    void login_validCredentials_success() {
        LoginResponse response = authService.login(new LoginRequest("admin", "1234"));

        assertThat(response.getUsername()).isEqualTo("admin");
        assertThat(response.getRole()).isEqualTo("ADMIN");
        assertThat(response.getMessage()).isEqualTo("Login successful");
    }

    @Test
    void login_wrongPassword_rejected() {
        assertThatThrownBy(() -> authService.login(new LoginRequest("admin", "wrongpass")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_unknownUsername_rejected() {
        assertThatThrownBy(() -> authService.login(new LoginRequest("admin1", "1234")))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
