package personal.bookerav2.dto.wrappers;

import personal.bookerav2.dto.books.BookAuthorDto;
import personal.bookerav2.dto.books.BookDtoResponse;
import personal.bookerav2.entities.Author;
import personal.bookerav2.entities.Book;

import java.util.stream.Collectors;

public class BookWrapper {
    public static BookDtoResponse toDto(Book book){
        return new BookDtoResponse(
                book.getBookId(),
                book.getName(),
                book.getIsbn(),
                book.getTotalPages(),
                book.getDescription(),
                book.getAuthors()
                        .stream()
                        .map(BookWrapper::toAuthorDto)
                        .collect(Collectors.toSet()),
                book.getCategories()
        );
    }

    private static BookAuthorDto toAuthorDto(Author author){
        return new BookAuthorDto(
                author.getAuthorId(),
                author.getName(),
                author.getSurname()
        );
    }
}
