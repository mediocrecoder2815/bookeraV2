package personal.bookerav2.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDtoRequest (
        @NotBlank @Size(max = 50) String username,
        @NotBlank String password
){
}
