package personal.bookerav2.dto.books;

import personal.bookerav2.dto.reviews.ReviewBookDto;
import personal.bookerav2.entities.Category;

import java.util.Set;


public record BookDtoResponse (
        Long bookId,
        String name,
        String isbn,
        Short totalPages,
        String description,
        Set<BookAuthorDto> authors,
        Double rating,
        Set<Category> categorySet,
        Set<ReviewBookDto> reviews
){
}
