package personal.bookerav2.dto.authors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AuthorDtoRequest(
        @Size(max = 25) String name,
        @Size(max = 25) String surname,
        String description,
        String pictureUrl,
        @Size(min = 2, max = 2) String countryCode,
        @NotNull LocalDate dateOfBirth
) {
}
