package personal.bookerav2.dto.reviews;

import java.util.UUID;

public record ReviewUserDto(
        UUID userId,
        String username,
        String avatarUrl
) {
}
