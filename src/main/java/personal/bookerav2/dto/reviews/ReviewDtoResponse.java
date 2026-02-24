package personal.bookerav2.dto.reviews;

import java.util.UUID;

public record ReviewDtoResponse(
        UUID reviewId,
        UUID userId,
        String content,
        Integer rating
) {
}
