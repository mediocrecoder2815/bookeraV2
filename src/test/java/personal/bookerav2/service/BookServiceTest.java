package personal.bookerav2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import personal.bookerav2.dto.books.BookAuthorDto;
import personal.bookerav2.dto.books.BookDtoAll;
import personal.bookerav2.dto.books.BookDtoRequest;
import personal.bookerav2.dto.books.BookDtoResponse;
import personal.bookerav2.dto.reviews.ReviewBookDto;
import personal.bookerav2.dto.wrappers.BookMapper;
import personal.bookerav2.entities.Author;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.Category;
import personal.bookerav2.repository.AuthorRepository;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.CategoryRepository;

import java.time.LocalDate;
import java.util.*;

import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.ReviewRepository;
import personal.bookerav2.repository.UserBookRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService unit tests")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserBookRepository userBookRepository;

    @InjectMocks
    private BookService bookService;



    private Author author;
    private Book book;
    private BookDtoRequest bookDtoRequest;
    private BookDtoRequest bookDtoRequestWithCategories;
    private Category category;


    @BeforeEach
    void setUp() {
        author = Author.builder()
                .authorId(1L)
                .name("George")
                .surname("Orwell")
                .build();

        category = Category.builder()
                .categoryId(1)
                .categoryName("Dystopian")
                .build();

        book = new Book();
        book.setBookId(1L);
        book.setName("1984");
        book.setIsbn("1234567890");
        book.setTotalPages((short) 328);
        book.setDescription("Dystopian novel");
        book.setDateOfPublish(LocalDate.of(1949, 6, 8));
        book.setAuthors(new HashSet<>(Set.of(author)));
        book.setCategories(new HashSet<>(Set.of(category)));
        book.setReviews(new HashSet<>());

        bookDtoRequest = new BookDtoRequest(
                "1984",
                "1234567890",
                 (short) 328,
                "Dystopian novel",
                LocalDate.of(1949, 6, 8),
                1L,
                "example.org/author_picture",
                null
        );

        bookDtoRequestWithCategories = new BookDtoRequest(
                "1984",
                "1234567890",
                 (short) 328,
                "Dystopian novel",
                LocalDate.of(1949, 6, 8),
                1L,
                "picture",
                new HashSet<>(Set.of(1))
        );
    }

    @Nested
    @DisplayName("getBookById")
    class GetBookById {

        @Test
        void shouldReturnBookWhenFound() {
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(reviewRepository.findReviewCountByBookId(1L)).thenReturn(1);
            when(reviewRepository.findAverageRatingByBookId(1L)).thenReturn(Optional.of(4.5));
            when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(reviewRepository.findByBook(any(Book.class))).thenReturn(Collections.emptySet());

            try (var mapper = mockStatic(BookMapper.class)) {
                BookDtoResponse expected = new BookDtoResponse(
                        1L, "1984", null, "1234567890",  (short) 328, "Dystopian novel",
                        Set.of(new BookAuthorDto(1L, "George", "Orwell")),
                        null,null,
                        new HashSet<>(Set.of(category)),
                        new HashSet<>(),
                        new int[5]
                );
                mapper.when(() -> BookMapper.toResponseDto(eq(book), any(), any())).thenReturn(expected);

                BookDtoResponse result = bookService.getBookById(1L);

                assertNotNull(result);
                assertEquals(expected, result);
            }
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(bookRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> bookService.getBookById(99L));
        }
    }

    @Nested
    @DisplayName("getAllBooks")
    class GetAllBooks {

        @Test
        void shouldReturnPagedBooks() {
            Page<Book> bookPage = new PageImpl<>(List.of(book));
            PageRequest pageable = PageRequest.of(0, 10);
            when(bookRepository.findAll(pageable)).thenReturn(bookPage);

            Page<BookDtoAll> result = bookService.getAllBooks(pageable);

            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            assertEquals("1984", result.getContent().getFirst().name());
        }
    }

    @Nested
    @DisplayName("createBook")
    class CreateBook {

        @Test
        void shouldCreateBookWithoutCategories() {
            when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
            when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

            try (var mapper = mockStatic(BookMapper.class)) {
                Book newBook = new Book();
                newBook.setName("1984");
                newBook.setIsbn("1234567890");
                newBook.setTotalPages((short) 328);
                newBook.setDescription("Dystopian novel");
                newBook.setDateOfPublish(LocalDate.of(1949, 6, 8));

                BookDtoResponse expected = new BookDtoResponse(
                        1L, "1984", null, "1234567890",  (short) 328, "Dystopian novel",
                        Set.of(new BookAuthorDto(1L, "George", "Orwell")),
                        null,null,
                        new HashSet<>(Set.of(category)),
                        new HashSet<>(),
                        new int[5]
                );

                mapper.when(() -> BookMapper.toBook(bookDtoRequest)).thenReturn(newBook);
                mapper.when(() -> BookMapper.toResponseDto(any(Book.class), any(), any())).thenReturn(expected);

                BookDtoResponse result = bookService.createBook(bookDtoRequest);

                assertNotNull(result);
                assertEquals(expected, result);
                verify(bookRepository).save(any(Book.class));
            }
        }

        @Test
        void shouldCreateBookWithCategories() {
            when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
            when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
            when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

            try (var mapper = mockStatic(BookMapper.class)) {
                Book newBook = new Book();
                newBook.setName("1984");
                newBook.setIsbn("1234567890");
                newBook.setTotalPages((short) 328);
                newBook.setDescription("Dystopian novel");
                newBook.setDateOfPublish(LocalDate.of(1949, 6, 8));

                BookDtoResponse expected = new BookDtoResponse(
                        1L, "1984", null, "1234567890",  (short) 328, "Dystopian novel",
                        Set.of(new BookAuthorDto(1L, "George", "Orwell")),
                        null,null,
                        new HashSet<>(Set.of(category)),
                        new HashSet<>(),
                        new int[5]
                );

                mapper.when(() -> BookMapper.toBook(bookDtoRequestWithCategories)).thenReturn(newBook);
                mapper.when(() -> BookMapper.toResponseDto(any(Book.class), any(), any())).thenReturn(expected);

                BookDtoResponse result = bookService.createBook(bookDtoRequestWithCategories);

                assertNotNull(result);
                assertEquals(expected, result);
                verify(categoryRepository).findById(1);
                verify(bookRepository).save(any(Book.class));
            }
        }
    }

    @Nested
    @DisplayName("updateBook")
    class UpdateBook {

        @Test
        void shouldUpdateBookSuccessfully() {
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

            try (var mapper = mockStatic(BookMapper.class)) {
                BookDtoResponse expected = new BookDtoResponse(
                        1L, "1984", null, "1234567890",  (short) 328, "Dystopian novel",
                        Set.of(new BookAuthorDto(1L, "George", "Orwell")),
                        null,null,
                        new HashSet<>(Set.of(category)),
                        new HashSet<>(),
                        new int[5]
                );
                mapper.when(() -> BookMapper.toResponseDto(any(Book.class), any(), any())).thenReturn(expected);

                BookDtoResponse result = bookService.updateBook(bookDtoRequest, 1L);

                assertNotNull(result);
                assertEquals(expected, result);
                verify(bookRepository).findById(1L);
                verify(bookRepository).save(any(Book.class));
            }
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(bookRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class,
                    () -> bookService.updateBook(bookDtoRequest, 99L));
        }
    }

    @Nested
    @DisplayName("deleteBookById")
    class DeleteBookById {

        @Test
        void shouldDeleteBookSuccessfully() {
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

            bookService.deleteBookById(1L);

            verify(bookRepository).findById(1L);
            verify(bookRepository).delete(book);
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(bookRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> bookService.deleteBookById(99L));
        }
    }
}
