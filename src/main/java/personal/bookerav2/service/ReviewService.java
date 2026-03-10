package personal.bookerav2.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import personal.bookerav2.dto.reviews.ReviewDtoRequest;
import personal.bookerav2.dto.reviews.ReviewDtoResponse;
import personal.bookerav2.dto.wrappers.ReviewWrapper;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.Review;
import personal.bookerav2.entities.User;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.ReviewRepository;
import personal.bookerav2.repository.UserRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReviewDtoResponse createReview(ReviewDtoRequest r){
        Review review = new Review();
        Book bookToFind = bookRepository.findById(r.bookId()).orElseThrow();
        User userToFind = userRepository.findById(r.userId()).orElseThrow();
        review.setContent(r.content());
        review.setBook(bookToFind);
        review.setUser(userToFind);
        review.setRating(r.rating());
        return ReviewWrapper.toReviewDtoResponse(reviewRepository.save(review));
    }
    public ReviewDtoResponse updateReview(ReviewDtoRequest r, UUID reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        review.setRating(r.rating());
        review.setContent(r.content());
        return ReviewWrapper.toReviewDtoResponse(reviewRepository.save(review));
    }
    public void deleteReview(UUID id){
        Review r = reviewRepository.findById(id).orElseThrow();
        reviewRepository.delete(r);
    }
    public ReviewDtoResponse getReviewById(UUID id){
        return ReviewWrapper.toReviewDtoResponse(reviewRepository.findById(id).orElseThrow());
    }
}
