package personal.bookerav2.dto.books;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public record BookDtoRequest(
        @NotBlank @Size(max =50) String name,
        @NotBlank @Size(max = 14) String isbn,
        Integer totalPages,
        String description,
        @NotNull Instant dateOfPublish,
        @NotNull Integer authorId,
        String pictureUrl,
        Optional<Set<Integer>> categoriesId
) {
}
