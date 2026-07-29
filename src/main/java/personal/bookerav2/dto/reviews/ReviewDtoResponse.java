package personal.bookerav2.dto.reviews;

import java.util.UUID;

public record ReviewDtoResponse(
        Long reviewId,
        UUID userId,
        Integer bookId,
        String content,
        Integer rating
) {
}
