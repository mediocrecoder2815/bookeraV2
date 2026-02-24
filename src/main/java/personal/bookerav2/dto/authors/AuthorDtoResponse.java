package personal.bookerav2.dto.authors;

import personal.bookerav2.entities.utils.CountryCode;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record AuthorDtoResponse(
        UUID authorId,
        String name,
        String surname,
        String description,
        CountryCode countryCode,
        Instant dateOfBirth,
        Set<AuthorBookDto> books
) {
}
