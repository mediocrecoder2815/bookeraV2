package personal.bookerav2.dto.wrappers;

import personal.bookerav2.dto.user.UserBooksDto;
import personal.bookerav2.dto.user.UserDtoResponse;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.User;
import personal.bookerav2.entities.UserBook;
import personal.bookerav2.entities.enums.BookStatus;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    private final static Map<Short, BookStatus> statusMap = Map.of(
            (short) 1, BookStatus.IN_PLANS,
            (short) 2, BookStatus.READING,
            (short) 3, BookStatus.DONE
    );
    public static UserDtoResponse toUserDtoResponse(User user, Set<Book> books, Set<UserBook> userBooks){
        return new UserDtoResponse(
                user.getUsername(),
                user.getAvatarUrl(),
                books.
                        stream().
                        map(b -> new UserBooksDto(
                                b.getBookId(),
                                b.getName(),
                                b.getPictureUrl(),
                                b.getAuthors().
                                        stream()
                                        .map(a -> a.getFullName())
                                        .toList(),
                                        statusMap.get(userBooks.stream().
                                                filter(ub -> ub.getBookId().equals(b))
                                                .findAny().get()
                                                .getBookStatus())
                                        )


                                ).collect(Collectors.toSet()),
                                user.getName(),
                                user.getSurname()
        );
    }
}
