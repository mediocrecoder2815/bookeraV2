package personal.bookerav2.dto.user;

import java.util.Set;

public record UserDtoResponse(
        String username,
        String avatarUrl,
        Set<UserBooksDto> userBooks,
        String name,
        String surname
) {
}
