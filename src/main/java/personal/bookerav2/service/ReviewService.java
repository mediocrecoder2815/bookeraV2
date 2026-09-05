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

import java.security.Principal;
import java.util.UUID;

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
        review.setBook(bookToFind);
        review.setUser(userToFind);
        return ReviewMapper.toReviewDtoResponse(reviewRepository.save(review));
    }
    @Transactional
    public ReviewDtoResponse updateReview(ReviewDtoRequest r, Long reviewId, Principal principal){
        Review review = findReviewById(reviewId);
        if(!principal.getName().equals(review.getUser().getUsername())){
            throw new InvalidCredentialsException("Wrong user");
        }
        review.setRating(r.rating());
        review.setContent(r.content());
        return ReviewMapper.toReviewDtoResponse(reviewRepository.save(review));
    }
    public void deleteReview(Long id, Principal principal){
        Review r = findReviewById(id);
        String ownerUsername = r.getUser().getUsername();
        if (!r.getUser().getUsername().equals(principal.getName())){
            throw new InvalidCredentialsException("Nice try");
        }
        reviewRepository.delete(r);
    }
    public ReviewDtoResponse getReviewById(Long id){
        return ReviewMapper.toReviewDtoResponse(findReviewById(id));
    }


    // helpers
    private Book findBookById(long id){
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

    private User findUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFound("User with such username " + username + "doesn't exists!")
        );
    }
}
