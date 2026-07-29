package personal.bookerav2.dto.books;

import java.util.UUID;

public record BookDtoAll(
        Integer bookId,
        String name,
        String pictureUrl
) {
}
