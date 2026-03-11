package personal.bookerav2.dto.authors;

import personal.bookerav2.entities.enums.CountryCode;

import java.time.Instant;

public record AuthorDtoRequest(
        String name,
        String surname,
        String description,
        CountryCode countryCode,
        Instant dateOfBirth
) {
}
