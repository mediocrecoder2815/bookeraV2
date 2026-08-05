package personal.bookerav2.dto.books;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookAuthorDto(
        @NotNull Long authorId,
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Size(max = 50)String surname
) {
}
