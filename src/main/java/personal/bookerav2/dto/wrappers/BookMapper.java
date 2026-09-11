package personal.bookerav2.dto.wrappers;

import personal.bookerav2.dto.books.BookAuthorDto;
import personal.bookerav2.dto.books.BookDtoAll;
import personal.bookerav2.dto.books.BookDtoRequest;
import personal.bookerav2.dto.books.BookDtoResponse;
import personal.bookerav2.entities.Author;
import personal.bookerav2.entities.Book;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookMapper {
    public static BookDtoResponse toResponseDto(Book book, Double avg, int[] rating){
        return new BookDtoResponse(
                book.getBookId(),
                book.getName(),
                book.getIsbn(),
                book.getTotalPages(),
                book.getDescription(),
                book.getAuthors()
                        .stream()
                        .map(BookMapper::toAuthorDto)
                        .collect(Collectors.toSet()),
                avg,
                book.getReviewCount(),
                book.getCategories(),
                book.getReviews()
                        .stream()
                        .map(ReviewMapper::toUserDto)
                        .collect(Collectors.toSet()),
                rating
        );
    }

    public static List<BookDtoAll> toBookAll(Set<Book> books){
        return books.stream().map(BookMapper::toAllBookDto).toList();
    }

    public static BookDtoAll toAllBookDto(Book book){
        return new BookDtoAll(book.getBookId(), book.getName(), book.getPictureUrl(), book.getAvgRating());
    }
    private static BookAuthorDto toAuthorDto(Author author){
        return new BookAuthorDto(
                author.getAuthorId(),
                author.getName(),
                author.getSurname()
        );
    }
    public static Book toBook(BookDtoRequest book){
        return Book.builder()
                .name(book.name())
                .totalPages(book.totalPages())
                .description(book.description())
                .isbn(book.isbn())
                .dateOfPublish(book.dateOfPublish())
                .pictureUrl(book.pictureUrl())

                .build();
    }

}
