package personal.bookerav2.dto.wrappers;

import org.springframework.stereotype.Component;
import personal.bookerav2.dto.reviews.ReviewDtoResponse;
import personal.bookerav2.entities.Review;


@Component
public class ReviewWrapper {
    public static ReviewDtoResponse toReviewDtoResponse(Review r){
        return new ReviewDtoResponse(
                r.getReviewId(),
                r.getUser().getUserId(),
                r.getContent(),
                r.getRating()
        );
    }
}
