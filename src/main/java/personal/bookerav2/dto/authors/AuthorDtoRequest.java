package personal.bookerav2.dto.authors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import personal.bookerav2.entities.enums.CountryCode;

import java.time.Instant;

public record AuthorDtoRequest(
        @Size(max = 50) String name,
        @Size(max = 50) String surname,
        String description,
        String pictureUrl,
        @Size(min = 2, max = 2) String countryCode,
        @NotNull Instant dateOfBirth
) {
}
