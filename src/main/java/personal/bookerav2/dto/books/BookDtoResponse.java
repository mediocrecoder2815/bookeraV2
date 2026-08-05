package personal.bookerav2.dto.books;

import personal.bookerav2.dto.reviews.ReviewBookDto;
import personal.bookerav2.entities.Category;

import java.util.Set;

public record BookDtoResponse (
        Integer bookId,
        String name,
        String isbn,
        Integer totalPages,
        String description,
        Set<BookAuthorDto> authors,
        Set<Category> categorySet,
        Set<ReviewBookDto> reviews
){
}
