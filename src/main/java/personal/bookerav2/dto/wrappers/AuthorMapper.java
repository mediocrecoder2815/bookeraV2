package personal.bookerav2.dto.wrappers;

import personal.bookerav2.dto.authors.AuthorBookDto;
import personal.bookerav2.dto.authors.AuthorDtoAll;
import personal.bookerav2.dto.authors.AuthorDtoRequest;
import personal.bookerav2.dto.authors.AuthorDtoResponse;
import personal.bookerav2.entities.Author;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.enums.CountryCode;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorMapper {
    public static Author toAuthor(AuthorDtoRequest a){
        return Author.builder()
                .name(a.name())
                .surname(a.surname())
                .description(a.description())
                .dateOfBirth(a.dateOfBirth())
                .country(CountryCode.convert(a.countryCode()))
                .pictureUrl(a.pictureUrl())
                .build();
    }

    public static AuthorDtoResponse toResponseDto(Author author){
        return new AuthorDtoResponse(
                author.getAuthorId(),
                author.getName(),
                author.getSurname(),
                author.getDescription(),
                author.getCountry(),
                author.getDateOfBirth(),
                author.getPictureUrl(),
                author.getBooks().
                        stream().
                        map(AuthorMapper::toAuthorBookDto).
                        collect(Collectors.toSet())
        );
    }
    public static AuthorDtoAll toAuthorDtoAll(Author a){
        return new AuthorDtoAll(a.getAuthorId(),
                                a.getName(),
                                a.getSurname(),
                                a.getPictureUrl());
    }
    public static List<AuthorDtoAll> toAuthorDtoList(List<Author> authors){
        return authors
                .stream()
                .map(AuthorMapper::toAuthorDtoAll)
                .toList();
    }

    public static AuthorBookDto toAuthorBookDto(Book book){
        return new AuthorBookDto(
                book.getBookId(),
                book.getName(),
                book.getPictureUrl(),
                book.getIsbn()
        );
    }

}
