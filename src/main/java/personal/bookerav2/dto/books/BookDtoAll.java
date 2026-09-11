package personal.bookerav2.dto.books;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record BookDtoAll(
        @NotNull Long bookId,
        @NotBlank @Size(max = 50) String name,
        String pictureUrl,
        @Max(5) BigDecimal rating
) {
}
