package personal.bookerav2.dto.user;

import java.util.Set;
import java.util.UUID;

public record UserShelfDto(
        UUID userId,
        Set<UserBooksDto> books
) {
}
