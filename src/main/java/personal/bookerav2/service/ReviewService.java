package personal.bookerav2.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.bookerav2.dto.reviews.ReviewDtoRequest;
import personal.bookerav2.dto.reviews.ReviewDtoResponse;
import personal.bookerav2.dto.wrappers.ReviewMapper;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.Review;
import personal.bookerav2.entities.User;
import personal.bookerav2.exceptions.ResourceNotFound;
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

    @Transactional
    public ReviewDtoResponse createReview(ReviewDtoRequest r, Integer bookId){
        Review review = new Review();
        Book bookToFind = findBookById(bookId);
        User userToFind = findUserById(r.userId());
        review.setContent(r.content());
        review.setBook(bookToFind);
        review.setUser(userToFind);
        review.setRating(r.rating());
        return ReviewMapper.toReviewDtoResponse(reviewRepository.save(review));
    }
    @Transactional
    public ReviewDtoResponse updateReview(ReviewDtoRequest r, Long reviewId){
        Review review = findReviewById(reviewId);
        review.setRating(r.rating());
        review.setContent(r.content());
        return ReviewMapper.toReviewDtoResponse(reviewRepository.save(review));
    }
    public void deleteReview(Long id){
        Review r = findReviewById(id);
        reviewRepository.delete(r);
    }
    public ReviewDtoResponse getReviewById(Long id){
        return ReviewMapper.toReviewDtoResponse(findReviewById(id));
    }

    private Book findBookById(int id){
        return bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Book with id + " + id + " doesn't exists")
        );
    }
    private User findUserById(UUID id){
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("User with id " + id + " doesn't exists")
        );
    }

    private Review findReviewById(long id){
        return reviewRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Review with id " + id + " doesn't exists")
        );
    }
}
