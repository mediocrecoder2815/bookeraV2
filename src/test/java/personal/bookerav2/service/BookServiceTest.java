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

import java.time.Instant;
import java.util.*;

import personal.bookerav2.exceptions.ResourceNotFound;
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
                .authorId(1)
                .name("George")
                .surname("Orwell")
                .build();

        category = Category.builder()
                .categoryId(1)
                .categoryName("Dystopian")
                .build();

        book = new Book();
        book.setBookId(1);
        book.setName("1984");
        book.setIsbn("1234567890");
        book.setTotalPages(328);
        book.setDescription("Dystopian novel");
        book.setDateOfPublish(Instant.parse("1949-06-08T00:00:00Z"));
        book.setAuthors(new HashSet<>(Set.of(author)));
        book.setCategories(new HashSet<>(Set.of(category)));
        book.setReviews(new HashSet<>());

        bookDtoRequest = new BookDtoRequest(
                "1984",
                "1234567890",
                328,
                "Dystopian novel",
                Instant.parse("1949-06-08T00:00:00Z"),
                1,
                Optional.empty()
        );

        bookDtoRequestWithCategories = new BookDtoRequest(
                "1984",
                "1234567890",
                328,
                "Dystopian novel",
                Instant.parse("1949-06-08T00:00:00Z"),
                1,
                Optional.of(new HashSet<>(Set.of(1)))
        );
    }

    @Nested
    @DisplayName("getBookById")
    class GetBookById {

        @Test
        void shouldReturnBookWhenFound() {
            when(bookRepository.findById(1)).thenReturn(Optional.of(book));

            try (var mapper = mockStatic(BookMapper.class)) {
                BookDtoResponse expected = new BookDtoResponse(
                        1, "1984", "1234567890", 328, "Dystopian novel",
                        Set.of(new BookAuthorDto(1, "George", "Orwell")),
                        new HashSet<>(Set.of(category)),
                        new HashSet<>()
                );
                mapper.when(() -> BookMapper.toResponseDto(book)).thenReturn(expected);

                BookDtoResponse result = bookService.getBookById(1);

                assertNotNull(result);
                assertEquals(expected, result);
            }
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(bookRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> bookService.getBookById(99));
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
            when(authorRepository.findById(1)).thenReturn(Optional.of(author));
            when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

            try (var mapper = mockStatic(BookMapper.class)) {
                Book newBook = new Book();
                newBook.setName("1984");
                newBook.setIsbn("1234567890");
                newBook.setTotalPages(328);
                newBook.setDescription("Dystopian novel");
                newBook.setDateOfPublish(Instant.parse("1949-06-08T00:00:00Z"));

                BookDtoResponse expected = new BookDtoResponse(
                        1, "1984", "1234567890", 328, "Dystopian novel",
                        Set.of(new BookAuthorDto(1, "George", "Orwell")),
                        new HashSet<>(),
                        new HashSet<>()
                );

                mapper.when(() -> BookMapper.toBook(bookDtoRequest)).thenReturn(newBook);
                mapper.when(() -> BookMapper.toResponseDto(any(Book.class))).thenReturn(expected);

                BookDtoResponse result = bookService.createBook(bookDtoRequest);

                assertNotNull(result);
                assertEquals(expected, result);
                verify(bookRepository).save(any(Book.class));
            }
        }

        @Test
        void shouldCreateBookWithCategories() {
            when(authorRepository.findById(1)).thenReturn(Optional.of(author));
            when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
            when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

            try (var mapper = mockStatic(BookMapper.class)) {
                Book newBook = new Book();
                newBook.setName("1984");
                newBook.setIsbn("1234567890");
                newBook.setTotalPages(328);
                newBook.setDescription("Dystopian novel");
                newBook.setDateOfPublish(Instant.parse("1949-06-08T00:00:00Z"));

                BookDtoResponse expected = new BookDtoResponse(
                        1, "1984", "1234567890", 328, "Dystopian novel",
                        Set.of(new BookAuthorDto(1, "George", "Orwell")),
                        new HashSet<>(Set.of(category)),
                        new HashSet<>()
                );

                mapper.when(() -> BookMapper.toBook(bookDtoRequestWithCategories)).thenReturn(newBook);
                mapper.when(() -> BookMapper.toResponseDto(any(Book.class))).thenReturn(expected);

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
            when(bookRepository.findById(1)).thenReturn(Optional.of(book));
            when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

            try (var mapper = mockStatic(BookMapper.class)) {
                BookDtoResponse expected = new BookDtoResponse(
                        1, "1984", "1234567890", 328, "Dystopian novel",
                        Set.of(new BookAuthorDto(1, "George", "Orwell")),
                        new HashSet<>(Set.of(category)),
                        new HashSet<>()
                );
                mapper.when(() -> BookMapper.toResponseDto(any(Book.class))).thenReturn(expected);

                BookDtoResponse result = bookService.updateBook(bookDtoRequest, 1);

                assertNotNull(result);
                assertEquals(expected, result);
                verify(bookRepository).findById(1);
                verify(bookRepository).save(any(Book.class));
            }
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(bookRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class,
                    () -> bookService.updateBook(bookDtoRequest, 99));
        }
    }

    @Nested
    @DisplayName("deleteBookById")
    class DeleteBookById {

        @Test
        void shouldDeleteBookSuccessfully() {
            when(bookRepository.findById(1)).thenReturn(Optional.of(book));

            bookService.deleteBookById(1);

            verify(bookRepository).findById(1);
            verify(bookRepository).delete(book);
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(bookRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> bookService.deleteBookById(99));
        }
    }
}
