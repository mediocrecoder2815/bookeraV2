package personal.bookerav2.dto.reviews;

import java.util.UUID;

public record ReviewDtoResponse(
        Long reviewId,
        UUID userId,
        Long bookId,
        String content,
        Short rating
) {
}
