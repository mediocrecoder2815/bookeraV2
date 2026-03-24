package personal.bookerav2.dto.reviews;

import java.util.UUID;

public record ReviewDtoResponse(
        UUID reviewId,
        UUID userId,
        UUID bookId,
        String content,
        Integer rating
) {
}
