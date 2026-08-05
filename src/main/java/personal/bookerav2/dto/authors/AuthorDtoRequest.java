package personal.bookerav2.dto.authors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import personal.bookerav2.entities.enums.CountryCode;

import java.time.Instant;

public record AuthorDtoRequest(
        @Size(max = 50) String name,
        @Size(max = 50) String surname,
        String description,
        @Size(max = 3) CountryCode countryCode,
        @NotBlank Instant dateOfBirth
) {
}
