package personal.bookerav2.dto.wrappers;

import personal.bookerav2.dto.user.UserBooksDto;
import personal.bookerav2.dto.user.UserDtoResponse;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.User;

import java.util.Set;

public class UserMapper {
    public static UserDtoResponse toUserDtoResponse(User user, Set<Book> books){
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
                                        .toList(), )

                                ).
        )
    }
}
