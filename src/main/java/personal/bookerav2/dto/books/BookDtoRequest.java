package personal.bookerav2.dto.books;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public record BookDtoRequest(
        String name,
        String isbn,
        Integer totalPages,
        String description,
        Instant dateOfPublish,
        Integer authorId,
        Optional<Set<Integer>> categoriesId
) {
}
