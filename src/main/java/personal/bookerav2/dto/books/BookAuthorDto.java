package personal.bookerav2.dto.books;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookAuthorDto(
        @NotBlank Integer authorId,
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Size(max = 50)String surname
) {
}
