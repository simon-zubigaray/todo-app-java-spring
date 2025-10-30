package jsz.myapp.todoapp.controller;

import jsz.myapp.todoapp.security.dto.AuthRequest;
import jsz.myapp.todoapp.security.dto.AuthResponse;
import jsz.myapp.todoapp.security.dto.RegisterRequest;
import jsz.myapp.todoapp.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    // Simulación de respuesta común
    private AuthResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = AuthResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .username("juan")
                .email("juan@mail.com")
                .fullName("Juan Zubigaray")
                .role("USER")
                .build();
    }

    // Test 1: Verifica que el controlador llame correctamente a AuthService.register()
    // y devuelva 200 OK con el AuthResponse esperado.
    @Test
    void register_ShouldReturnOkResponse() {
        RegisterRequest request = RegisterRequest.builder()
                .fullName("Juan Zubigaray")
                .username("juan")
                .email("juan@mail.com")
                .password("123456")
                .role("USER")
                .build();

        when(authService.register(request)).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(authService).register(request);
    }

    // Test 2: Verifica que el controlador llame correctamente a AuthService.login()
    // y devuelva 200 OK con el AuthResponse esperado.
    @Test
    void login_ShouldReturnOkResponse() {
        AuthRequest request = AuthRequest.builder()
                .username("juan")
                .password("123456")
                .build();

        when(authService.login(request)).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(authService).login(request);
    }

    // Test 3: Verifica que el controlador extraiga correctamente el token del header
    // y llame a AuthService.refreshToken() con el token sin el prefijo "Bearer ".
    @Test
    void refresh_ShouldCallServiceWithTokenWithoutBearerPrefix() {
        String header = "Bearer refresh-token";
        when(authService.refreshToken("refresh-token")).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.refresh(header);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(authService).refreshToken("refresh-token");
    }
}
