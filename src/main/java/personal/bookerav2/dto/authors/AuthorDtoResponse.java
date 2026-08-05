package personal.bookerav2.dto.authors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import personal.bookerav2.entities.enums.CountryCode;

import java.time.LocalDate;
import java.util.Set;

public record AuthorDtoResponse(
        @NotNull Long authorId,
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Size(max = 50) String surname,
        String description,
        @Size(max = 3) CountryCode countryCode,
        LocalDate dateOfBirth,
        String pictureUrl,
        Set<AuthorBookDto> books
) {
}
