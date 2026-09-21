package personal.bookerav2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import personal.bookerav2.dto.user.UserBookDtoRequest;
import personal.bookerav2.dto.user.UserDtoResponse;
import personal.bookerav2.entities.*;
import personal.bookerav2.exceptions.ResourceDuplicateException;
import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.ReviewRepository;
import personal.bookerav2.repository.UserBookRepository;
import personal.bookerav2.repository.UserRepository;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService unit tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserBookRepository userBookRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private Principal principal;

    @InjectMocks
    private UserService userService;

    private User user;
    private Book book;
    private UserBook userBook;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        user = new User();
        user.setUserId(userId);
        user.setUsername("johndoe");
        user.setName("John");
        user.setSurname("Doe");
        user.setAvatarUrl("http://avatar.url");

        book = new Book();
        book.setBookId(1L);
        book.setName("Test Book");
        book.setAuthors(new HashSet<>());
        book.setReviews(new HashSet<>());

        userBook = new UserBook();
        userBook.setUserId(user);
        userBook.setBookId(book);
        userBook.setBookStatus((short) 1);
        userBook.setUserBookId(new UserBookId(userId, 1L));
    }

    @Nested
    @DisplayName("getMe")
    class GetMe {

        @Test
        void shouldReturnUserProfile() {
            when(principal.getName()).thenReturn("johndoe");
            when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
            when(userBookRepository.findByUserId_UserId(userId)).thenReturn(Set.of(userBook));
            when(bookRepository.findAllById(any())).thenReturn(List.of(book));
            when(reviewRepository.findByUser(user)).thenReturn(new HashSet<>());

            UserDtoResponse result = userService.getMe(principal);

            assertNotNull(result);
            assertEquals("johndoe", result.username());
            assertEquals("John", result.name());
            assertEquals("Doe", result.surname());
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
            when(principal.getName()).thenReturn("unknown");

            assertThrows(ResourceNotFound.class, () -> userService.getMe(principal));
        }
    }

    @Nested
    @DisplayName("addBook")
    class AddBook {

        @Test
        void shouldAddBookToShelf() {
            UserBookDtoRequest request = new UserBookDtoRequest(1L, (short) 1);

            when(principal.getName()).thenReturn("johndoe");
            when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(userBookRepository.findByUserId_UserId(userId)).thenReturn(new HashSet<>());
            when(userBookRepository.save(any(UserBook.class))).thenReturn(userBook);
            when(reviewRepository.findByUser(user)).thenReturn(new HashSet<>());

            UserDtoResponse result = userService.addBook(principal, request);

            assertNotNull(result);
            verify(userBookRepository).save(any(UserBook.class));
        }

        @Test
        void shouldThrowWhenBookAlreadyInShelf() {
            UserBookDtoRequest request = new UserBookDtoRequest(1L, (short) 1);

            when(principal.getName()).thenReturn("johndoe");
            when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(userBookRepository.findByUserId_UserId(userId)).thenReturn(Set.of(userBook));

            assertThrows(ResourceDuplicateException.class,
                    () -> userService.addBook(principal, request));
            verify(userBookRepository, never()).save(any());
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            UserBookDtoRequest request = new UserBookDtoRequest(1L, (short) 1);
            when(principal.getName()).thenReturn("unknown");
            when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> userService.addBook(principal, request));
        }

        @Test
        void shouldThrowWhenBookNotFound() {
            UserBookDtoRequest request = new UserBookDtoRequest(99L, (short) 1);

            when(principal.getName()).thenReturn("johndoe");
            when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
            when(bookRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> userService.addBook(principal, request));
        }
    }

    @Nested
    @DisplayName("updateStatus")
    class UpdateStatus {

        @Test
        void shouldUpdateBookStatus() {
            UserBookDtoRequest request = new UserBookDtoRequest(1L, (short) 2);

            when(principal.getName()).thenReturn("johndoe");
            when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(userBookRepository.findByUserId_UserId(userId)).thenReturn(Set.of(userBook));
            when(userBookRepository.save(any(UserBook.class))).thenReturn(userBook);
            when(reviewRepository.findByUser(user)).thenReturn(new HashSet<>());

            UserDtoResponse result = userService.updateStatus(principal, request);

            assertNotNull(result);
            verify(userBookRepository).save(any(UserBook.class));
        }

        @Test
        void shouldThrowWhenStatusIdInvalidLow() {
            UserBookDtoRequest request = new UserBookDtoRequest(1L, (short) -1);

            assertThrows(IllegalArgumentException.class,
                    () -> userService.updateStatus(principal, request));
        }

        @Test
        void shouldThrowWhenStatusIdInvalidHigh() {
            UserBookDtoRequest request = new UserBookDtoRequest(1L, (short) 4);

            assertThrows(IllegalArgumentException.class,
                    () -> userService.updateStatus(principal, request));
        }

        @Test
        void shouldThrowWhenBookNotInShelf() {
            UserBookDtoRequest request = new UserBookDtoRequest(1L, (short) 2);

            when(principal.getName()).thenReturn("johndoe");
            when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(userBookRepository.findByUserId_UserId(userId)).thenReturn(new HashSet<>());

            assertThrows(ResourceNotFound.class,
                    () -> userService.updateStatus(principal, request));
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            UserBookDtoRequest request = new UserBookDtoRequest(1L, (short) 2);
            when(principal.getName()).thenReturn("unknown");
            when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> userService.updateStatus(principal, request));
        }

        @Test
        void shouldThrowWhenBookNotFound() {
            UserBookDtoRequest request = new UserBookDtoRequest(99L, (short) 2);

            when(principal.getName()).thenReturn("johndoe");
            when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
            when(bookRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> userService.updateStatus(principal, request));
        }
    }
}
