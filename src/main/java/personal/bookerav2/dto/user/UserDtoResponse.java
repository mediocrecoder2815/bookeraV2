package personal.bookerav2.dto.user;

import personal.bookerav2.dto.reviews.ReviewDtoResponse;

import java.util.Set;

public record UserDtoResponse(
        String username,
        String avatarUrl,
        Set<UserBooksDto> userBooks,
        Set<ReviewDtoResponse> reviews,
        String name,
        String surname
) {
}
