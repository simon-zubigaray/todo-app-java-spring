package jsz.myapp.todoapp.controller;


import jakarta.validation.Valid;
import jsz.myapp.todoapp.security.dto.AuthRequest;
import jsz.myapp.todoapp.security.dto.AuthResponse;
import jsz.myapp.todoapp.security.dto.RegisterRequest;
import jsz.myapp.todoapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        String token = refreshToken.substring(7); // Remover "Bearer "
        return ResponseEntity.ok(authService.refreshToken(token));
    }
}