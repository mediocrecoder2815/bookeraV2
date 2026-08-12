package personal.bookerav2.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank String password,
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Size(max = 50) String surname
) {
}