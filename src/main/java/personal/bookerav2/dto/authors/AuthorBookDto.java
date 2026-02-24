package personal.bookerav2.dto.authors;

import java.util.UUID;

public record AuthorBookDto (
        UUID bookId,
        String name,
        String isbn
){
}
