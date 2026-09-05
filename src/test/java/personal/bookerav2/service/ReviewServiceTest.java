package personal.bookerav2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewService unit tests")
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private Principal principal;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private Book book;
    private User user;
    private ReviewDtoRequest reviewDtoRequest;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        user = new User();
        user.setUserId(userId);
        user.setUsername("johndoe");
        user.setAvatarUrl("http://avatar.url");

        book = new Book();
        book.setBookId(1L);
        book.setName("Test Book");
        book.setReviews(new HashSet<>());

        review = new Review();
        review.setReviewId(1L);
        review.setContent("Great book");
        review.setRating((short) 5);
        review.setBook(book);
        review.setUser(user);

        reviewDtoRequest = new ReviewDtoRequest(
                "Great book",
                (short) 5
        );
    }

    @Nested
    @DisplayName("createReview")
    class CreateReview {

        @Test
        void shouldCreateReviewSuccessfully() {
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
            when(principal.getName()).thenReturn(user.getUsername());
            when(reviewRepository.save(any(Review.class))).thenReturn(review);

            try (var mapper = mockStatic(ReviewMapper.class)) {
                Review newReview = new Review();
                newReview.setContent("Great book");
                newReview.setRating((short) 5);
                mapper.when(() -> ReviewMapper.toReview(reviewDtoRequest)).thenReturn(newReview);

                ReviewDtoResponse expected = new ReviewDtoResponse(
                        1L, user.getUserId(), 1L, "Great book", (short) 5
                );
                mapper.when(() -> ReviewMapper.toReviewDtoResponse(any(Review.class))).thenReturn(expected);

                ReviewDtoResponse result = reviewService.createReview(reviewDtoRequest, 1L, principal);

                assertNotNull(result);
                assertEquals(expected, result);
                verify(bookRepository).findById(1L);
                verify(userRepository).findByUsername(user.getUsername());
                verify(reviewRepository).save(any(Review.class));
            }
        }

        @Test
        void shouldThrowWhenBookNotFound() {
            when(bookRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class,
                    () -> reviewService.createReview(reviewDtoRequest, 99L, principal));
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(principal.getName()).thenReturn(user.getUsername());
            when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class,
                    () -> reviewService.createReview(reviewDtoRequest, 1L, principal));
        }

        @Test
        void shouldThrowWhenPrincipalMismatch() {
            User otherUser = new User();
            otherUser.setUsername("someone_else");

            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(userRepository.findByUsername("someone_else")).thenReturn(Optional.of(otherUser));
            when(principal.getName()).thenReturn("someone_else");

            assertThrows(InvalidCredentialsException.class,
                    () -> reviewService.createReview(reviewDtoRequest, 1L, principal));
        }
    }

    @Nested
    @DisplayName("updateReview")
    class UpdateReview {

        @Test
        void shouldUpdateReviewSuccessfully() {
            when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
            when(principal.getName()).thenReturn(user.getUsername());
            when(reviewRepository.save(any(Review.class))).thenReturn(review);

            try (var mapper = mockStatic(ReviewMapper.class)) {
                ReviewDtoResponse expected = new ReviewDtoResponse(
                        1L, user.getUserId(), 1L, "Updated content", (short) 4
                );
                mapper.when(() -> ReviewMapper.toReviewDtoResponse(any(Review.class))).thenReturn(expected);

                ReviewDtoRequest updateRequest = new ReviewDtoRequest("Updated content", (short) 4);
                ReviewDtoResponse result = reviewService.updateReview(updateRequest, 1L, principal);

                assertNotNull(result);
                assertEquals(expected, result);
                verify(reviewRepository).findById(1L);
                verify(reviewRepository).save(any(Review.class));
            }
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class,
                    () -> reviewService.updateReview(reviewDtoRequest, 99L, principal));
        }

        @Test
        void shouldThrowWhenPrincipalMismatch() {
            when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
            when(principal.getName()).thenReturn("someone_else");

            assertThrows(InvalidCredentialsException.class,
                    () -> reviewService.updateReview(reviewDtoRequest, 1L, principal));
        }
    }

    @Nested
    @DisplayName("deleteReview")
    class DeleteReview {

        @Test
        void shouldDeleteReviewSuccessfully() {
            when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
            when(principal.getName()).thenReturn(user.getUsername());

            reviewService.deleteReview(1L, principal);

            verify(reviewRepository).findById(1L);
            verify(reviewRepository).delete(review);
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> reviewService.deleteReview(99L, principal));
        }

        @Test
        void shouldThrowWhenPrincipalMismatch() {
            when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
            when(principal.getName()).thenReturn("someone_else");

            assertThrows(InvalidCredentialsException.class,
                    () -> reviewService.deleteReview(1L, principal));
        }
    }

    @Nested
    @DisplayName("getReviewById")
    class GetReviewById {

        @Test
        void shouldReturnReviewWhenFound() {
            when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

            try (var mapper = mockStatic(ReviewMapper.class)) {
                ReviewDtoResponse expected = new ReviewDtoResponse(
                        1L, user.getUserId(), 1L, "Great book", (short) 5
                );
                mapper.when(() -> ReviewMapper.toReviewDtoResponse(review)).thenReturn(expected);

                ReviewDtoResponse result = reviewService.getReviewById(1L);

                assertNotNull(result);
                assertEquals(expected, result);
            }
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> reviewService.getReviewById(99L));
        }
    }
}
