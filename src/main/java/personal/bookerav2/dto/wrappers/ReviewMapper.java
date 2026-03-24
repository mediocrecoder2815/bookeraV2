package personal.bookerav2.dto.wrappers;

import org.springframework.stereotype.Component;
import personal.bookerav2.dto.reviews.ReviewDtoResponse;
import personal.bookerav2.dto.reviews.ReviewBookDto;
import personal.bookerav2.entities.Review;


@Component
public class ReviewMapper {
    public static ReviewDtoResponse toReviewDtoResponse(Review r){
        return new ReviewDtoResponse(
                r.getReviewId(),
                r.getBook().getBookId(),
                r.getUser().getUserId(),
                r.getContent(),
                r.getRating()
        );
    }
    public static ReviewBookDto toUserDto(Review r){
        return new ReviewBookDto(
                r.getUser().getUserId(),
                r.getUser().getUsername(),
                r.getUser().getAvatarUrl(),
                r.getContent(),
                r.getRating()
                );
    }
}
