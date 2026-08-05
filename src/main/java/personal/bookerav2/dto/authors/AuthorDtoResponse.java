package personal.bookerav2.dto.authors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import personal.bookerav2.entities.enums.CountryCode;

import java.time.Instant;
import java.util.Set;

public record AuthorDtoResponse(
        @NotBlank Integer authorId,
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Size(max = 50) String surname,
        String description,
        @Size(max = 3) CountryCode countryCode,
        Instant dateOfBirth,
        Set<AuthorBookDto> books
) {
}
