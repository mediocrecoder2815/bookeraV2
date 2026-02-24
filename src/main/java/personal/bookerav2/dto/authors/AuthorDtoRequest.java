package personal.bookerav2.dto.authors;

import personal.bookerav2.entities.utils.CountryCode;

import java.time.Instant;
import java.util.UUID;

public record AuthorDtoRequest(
        String name,
        String surname,
        String description,
        CountryCode countryCode,
        Instant dateOfBirth
) {
}
