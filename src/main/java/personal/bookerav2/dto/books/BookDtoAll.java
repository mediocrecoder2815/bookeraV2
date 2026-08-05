package personal.bookerav2.dto.books;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookDtoAll(
        @NotNull Long bookId,
        @NotBlank @Size(max = 50) String name,
        String pictureUrl
) {
}
