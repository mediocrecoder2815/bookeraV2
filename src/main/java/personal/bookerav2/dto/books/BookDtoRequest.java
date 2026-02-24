package personal.bookerav2.dto.books;

import java.util.Set;
import java.util.UUID;

public record BookDtoRequest(
        String name,
        String isbn,
        Integer totalPages,
        String description,
        UUID authorId,
        Set<Long> categoriesId
) {
}
