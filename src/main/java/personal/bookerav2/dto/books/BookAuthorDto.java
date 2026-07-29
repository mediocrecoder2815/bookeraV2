package personal.bookerav2.dto.books;

import java.util.UUID;

public record BookAuthorDto(
        Integer authorId,
        String name,
        String surname
) {
}
