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
import personal.bookerav2.exceptions.InvalidCredentialsException;
import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.ReviewRepository;
import personal.bookerav2.repository.UserRepository;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Set;

@Service
@AllArgsConstructor
public class ReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public ReviewDtoResponse createReview(ReviewDtoRequest r, Long bookId, Principal principal){
        Review review = ReviewMapper.toReview(r);
        Book bookToFind = findBookById(bookId);
        User userToFind = findUserByUsername(principal.getName());
        if(principal == null){
            throw new InvalidCredentialsException("NO token, no comment");
        }
        review.setBook(bookToFind);
        review.setUser(userToFind);
        Review reviewToSave = reviewRepository.save(review);
        calculateBookAvg(bookToFind);
        calculateReviewCount(bookToFind);
        return ReviewMapper.toReviewDtoResponse(reviewToSave);
    }
    @Transactional
    public ReviewDtoResponse updateReview(ReviewDtoRequest r, Long reviewId, Principal principal){
        Review review = findReviewById(reviewId);
        if(!principal.getName().equals(review.getUser().getUsername())){
            throw new InvalidCredentialsException("Wrong user");
        }
        review.setRating(r.rating());
        review.setContent(r.content());
        Review reviewToSave = reviewRepository.save(review);
        Book bookToChange = review.getBook();
        calculateBookAvg(bookToChange);
        return ReviewMapper.toReviewDtoResponse(reviewToSave);
    }

    public void deleteReview(Long id, Principal principal){
        Review r = findReviewById(id);
        if (!r.getUser().getUsername().equals(principal.getName())){
            throw new InvalidCredentialsException("Nice try");
        }
        Book deletedBookReview = r.getBook();
        reviewRepository.delete(r);
        calculateReviewCount(deletedBookReview);
        calculateBookAvg(deletedBookReview);
    }
    public ReviewDtoResponse getReviewById(Long id){
        return ReviewMapper.toReviewDtoResponse(findReviewById(id));
    }


    // helpers
    private Book findBookById(Long id){
        return bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Book with id " + id + " doesn't exists")
        );
    }

    private Review findReviewById(long id){
        return reviewRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Review with id " + id + " doesn't exists")
        );
    }

    private User findUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFound("User with such username " + username + "doesn't exists!")
        );
    }
    private void calculateBookAvg(Book book){
        Double reviewScore = reviewRepository.findAverageRatingByBookId(book.getBookId());
        book.setAvgRating(new BigDecimal(reviewScore));
        bookRepository.save(book);
    }
    private void calculateReviewCount(Book book){
        Integer count = reviewRepository.findReviewCountByBookId(book.getBookId());
        book.setReviewCount(count);
        bookRepository.save(book);
    }
}
