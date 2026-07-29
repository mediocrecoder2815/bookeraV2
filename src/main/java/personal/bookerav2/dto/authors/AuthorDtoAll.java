package personal.bookerav2.dto.authors;

import java.util.UUID;

public record AuthorDtoAll(
        Integer authorId,
        String name,
        String surname
) {
}
