package personal.bookerav2.dto.authors;

import java.util.UUID;

public record AuthorDtoAll(
        UUID authorId,
        String name,
        String surname
) {
}
