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
    void login_withValidCredentials_returnsLoginResponse() {
        LoginResponse response = authService.login(new LoginRequest("admin", "1234"));

        assertThat(response.getUsername()).isEqualTo("admin");
        assertThat(response.getRole()).isEqualTo("ADMIN");
        assertThat(response.getMessage()).isEqualTo("Login successful");
    }

    @Test
    void login_withWrongPassword_throwsInvalidCredentials() {
        assertThatThrownBy(() -> authService.login(new LoginRequest("admin", "wrongpass")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_withUnknownUsername_throwsInvalidCredentials() {
        assertThatThrownBy(() -> authService.login(new LoginRequest("admin1", "1234")))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
