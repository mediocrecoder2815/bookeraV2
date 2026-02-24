package personal.bookerav2.dto.books;

import java.util.UUID;

public record BookDtoAll(
        UUID bookId,
        String name,
        String pictureUrl
) {
}
