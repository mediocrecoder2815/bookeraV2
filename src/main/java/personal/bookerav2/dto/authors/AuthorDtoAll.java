package personal.bookerav2.dto.authors;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorDtoAll(
        @NotBlank Integer authorId,
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Size(max = 50) String surname
) {
}
