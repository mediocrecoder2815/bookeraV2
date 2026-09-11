package personal.bookerav2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.bookerav2.dto.auth.AuthResponse;
import personal.bookerav2.dto.auth.LoginRequest;
import personal.bookerav2.dto.auth.RegisterRequest;
import personal.bookerav2.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Auth", description = "Authentication endpoints (register and login)")
public class AuthController {
    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Takes: a RegisterRequest body.
     * Returns: an AuthResponse with the new user's JWT.
     */
    @Operation(summary = "Register a new user",
            description = "Creates a user account and returns a JWT for the new user.")
    @ApiResponse(responseCode = "200", description = "User registered, JWT returned",
            content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed or duplicates")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/login
     * Takes: a LoginRequest body.
     * Returns: an AuthResponse with the user's JWT.
     */
    @Operation(summary = "Login",
            description = "Authenticates a user and returns a JWT.")
    @ApiResponse(responseCode = "200", description = "Login successful, JWT returned",
            content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}