package personal.bookerav2.dto.wrappers;

import org.springframework.stereotype.Component;
import personal.bookerav2.dto.authors.AuthorBookDto;
import personal.bookerav2.dto.authors.AuthorDtoAll;
import personal.bookerav2.dto.authors.AuthorDtoResponse;
import personal.bookerav2.entities.Author;
import personal.bookerav2.entities.Book;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {
    public static AuthorDtoResponse toResponseDto(Author author){
        return new AuthorDtoResponse(
                author.getAuthorId(),
                author.getName(),
                author.getSurname(),
                author.getDescription(),
                author.getCountry(),
                author.getDateOfBirth(),
                author.getBooks().
                        stream().
                        map(AuthorMapper::toAuthorBookDto).
                        collect(Collectors.toSet())
        );
    }
    public static AuthorDtoAll toAuthorDtoAll(Author a){
        return new AuthorDtoAll(a.getAuthorId(),
                                a.getName(),
                                a.getSurname());
    }
    public static List<AuthorDtoAll> toAuthorDtoList(List<Author> authors){
        return authors
                .stream()
                .map(AuthorMapper::toAuthorDtoAll)
                .collect(Collectors.toUnmodifiableList());
    }

    public static AuthorBookDto toAuthorBookDto(Book book){
        return new AuthorBookDto(
                book.getBookId(),
                book.getName(),
                book.getIsbn()
        );
    }

}
