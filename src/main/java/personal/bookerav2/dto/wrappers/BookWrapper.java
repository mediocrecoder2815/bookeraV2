package personal.bookerav2.dto.wrappers;

import personal.bookerav2.dto.authors.AuthorBookDto;
import personal.bookerav2.dto.books.BookAuthorDto;
import personal.bookerav2.dto.books.BookDtoAll;
import personal.bookerav2.dto.books.BookDtoRequest;
import personal.bookerav2.dto.books.BookDtoResponse;
import personal.bookerav2.entities.Author;
import personal.bookerav2.entities.Book;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookWrapper {
    public static BookDtoResponse toResponseDto(Book book){
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
                book.getCategories(),
                book.getReviews()
                        .stream()
                        .map(ReviewWrapper::toReviewDtoResponse)
                        .collect(Collectors.toSet())
        );
    }

    public static List<BookDtoAll> toBookAll(Set<Book> books){
        return books.stream().map(BookWrapper::toAllBookDto).toList();
    }

    private static BookDtoAll toAllBookDto(Book book){
        return new BookDtoAll(book.getBookId(), book.getName(), book.getPictureUrl());
    }
    private static BookAuthorDto toAuthorDto(Author author){
        return new BookAuthorDto(
                author.getAuthorId(),
                author.getName(),
                author.getSurname()
        );
    }
    public static Book toBook(BookDtoRequest book){
        Book newBook = new Book();
        newBook.setName(book.name());
        newBook.setTotalPages(book.totalPages());
        newBook.setDescription(book.description());
        newBook.setIsbn(book.isbn());
        newBook.setDateOfPublish(book.dateOfPublish());
        return newBook;
    }

}
