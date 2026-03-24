package personal.bookerav2.dto.reviews;

import java.util.UUID;

public record ReviewBookDto(
        UUID userId,
        String username,
        String avatarUrl,
        String content,
        Integer rating
) {
}
