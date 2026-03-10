package personal.bookerav2.dto.books;

import personal.bookerav2.dto.reviews.ReviewDtoResponse;
import personal.bookerav2.entities.Category;

import java.util.Set;
import java.util.UUID;

public record BookDtoResponse (
        UUID bookId,
        String name,
        String isbn,
        Integer totalPages,
        String description,
        Set<BookAuthorDto> authors,
        Set<Category> categorySet,
        Set<ReviewDtoResponse> reviews
){
}
