package personal.bookerav2.dto.books;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookDtoAll(
        @NotBlank Integer bookId,
        @NotBlank @Size(max = 50) String name,
        String pictureUrl
) {
}
