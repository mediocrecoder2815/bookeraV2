package personal.bookerav2.dto.wrappers;

import personal.bookerav2.dto.reviews.ReviewDtoRequest;
import personal.bookerav2.dto.reviews.ReviewDtoResponse;
import personal.bookerav2.dto.reviews.ReviewBookDto;
import personal.bookerav2.entities.Review;


public class ReviewMapper {
    public static Review toReview(ReviewDtoRequest r){
        Review review = new Review();
        review.setContent(r.content());
        review.setRating(r.rating());
        return review;
    }

    public static ReviewDtoResponse toReviewDtoResponse(Review r){
        return new ReviewDtoResponse(
                r.getReviewId(),
                r.getUser().getUserId(),
                r.getBook().getBookId(),
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
