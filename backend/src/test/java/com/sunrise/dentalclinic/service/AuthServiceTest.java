package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.request.LoginRequest;
import com.sunrise.dentalclinic.dto.request.RegisterRequest;
import com.sunrise.dentalclinic.dto.response.LoginResponse;
import com.sunrise.dentalclinic.entity.User;
import com.sunrise.dentalclinic.exception.InvalidCredentialsException;
import com.sunrise.dentalclinic.exception.UsernameAlreadyExistsException;
import com.sunrise.dentalclinic.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_validCredentials_success() {
        UsernamePasswordAuthenticationToken authenticated =
                new UsernamePasswordAuthenticationToken("admin", "admin123",
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        when(authenticationManager.authenticate(any())).thenReturn(authenticated);

        LoginResponse response = authService.login(new LoginRequest("admin", "admin123"));

        assertThat(response.getUsername()).isEqualTo("admin");
        assertThat(response.getRole()).isEqualTo("ADMIN");
        assertThat(response.getMessage()).isEqualTo("Login successful");
    }

    @Test
    void login_wrongPassword_rejected() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));

        assertThatThrownBy(() -> authService.login(new LoginRequest("admin", "wrongpass")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_unknownUsername_rejected() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));

        assertThatThrownBy(() -> authService.login(new LoginRequest("nouser", "admin123")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void register_newUsername_created() {
        when(userRepository.findByUsername("receptionist1")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass1234")).thenReturn("hashed-pass1234");

        LoginResponse response = authService.register(
                new RegisterRequest("receptionist1", "pass1234", "receptionist"));

        assertThat(response.getUsername()).isEqualTo("receptionist1");
        assertThat(response.getRole()).isEqualTo("RECEPTIONIST");
        assertThat(response.getMessage()).isEqualTo("Registration successful");
    }

    @Test
    void register_existingUsername_rejected() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> authService.register(new RegisterRequest("admin", "pass1234", "admin")))
                .isInstanceOf(UsernameAlreadyExistsException.class);
    }
}
