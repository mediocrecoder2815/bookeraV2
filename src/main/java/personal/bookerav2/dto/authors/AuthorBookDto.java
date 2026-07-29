package personal.bookerav2.dto.authors;

import java.util.UUID;

public record AuthorBookDto (
        Integer bookId,
        String name,
        String isbn
){
}
