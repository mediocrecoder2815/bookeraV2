package personal.bookerav2.dto.wrappers;

import personal.bookerav2.dto.user.UserBooksDto;
import personal.bookerav2.dto.user.UserDtoResponse;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.User;
import personal.bookerav2.entities.UserBook;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDtoResponse toUserDtoResponse(User user, Set<Book> books, Set<UserBook> userBooks){

        return new UserDtoResponse(
                user.getUsername(),
                user.getAvatarUrl(),
                books.
                        stream().
                        map(b -> new UserBooksDto(
                                b.getName(),
                                b.getPictureUrl(),
                                b.getAuthors().
                                        stream()
                                        .map(a -> a.getFullName())
                                        .toList()
                                        )

                                ).collect(Collectors.toSet()),
                                user.getName(),
                                user.getSurname()
        );
    }
}
