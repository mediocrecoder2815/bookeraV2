//package personal.bookerav2.service;
//
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import personal.bookerav2.dto.books.BookDtoRequest;
//import personal.bookerav2.dto.books.BookDtoResponse;
//import personal.bookerav2.dto.wrappers.BookMapper;
//import personal.bookerav2.entities.Author;
//import personal.bookerav2.entities.Book;
//import personal.bookerav2.entities.Category;
//import personal.bookerav2.repository.AuthorRepository;
//import personal.bookerav2.repository.BookRepository;
//import personal.bookerav2.repository.CategoryRepository;
//
//import java.time.Instant;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("BookService unit tests")
//class BookServiceTest {
//
//    @Mock
//    private AuthorRepository authorRepository;
//    @Mock
//    private BookRepository bookRepository;
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @InjectMocks
//    private BookService bookService;
//
//    private Author author;
//    private BookDtoRequest bookDtoRequest;
//    private BookDtoResponse bookDtoResponse;
//    private Set<Category> categories;
//
//    @BeforeEach
//    void setUp(){
//        this.categories = new LinkedHashSet<>();
//        this.categories.add(new Category()
//                    .builder()
//                    .categoryId(1L)
//                    .categoryName("Fantasy")
//                    .build());
//        this.categories.add(
//                new Category()
//                        .builder()
//                        .categoryName("Sci-fi")
//                        .categoryId(2L)
//                        .build());
//        this.author = Author.builder()
//                .authorId(UUID.fromString("MOCK"))
//                .name("George")
//                .surname("Floyd")
//                .build();
//        this.bookDtoRequest = new BookDtoRequest(
//                "White nights",
//                "GFOKMSADG014",
//                120,
//                "White nights by ...",
//                Instant.now(),
//                UUID.fromString("MOCK"),
//                Optional.of(categories.stream().map(Category::getCategoryId).collect(Collectors.toSet()))
//        );
//        Book book = new Book().builder().bookId(UUID.fromString("MOCKITO"))
//                .isbn("GFOKMSADG014")
//                .name("White nights")
//                .totalPages(120)
//                .description("White nights by ...")
//                .
//    }
//
//    @Nested
//    @DisplayName("creation tests")
//    class createBook{
//        @Test
//        void shouldCreateBook_WhenFieldsValid(){
//            //Given
//            when(BookMapper.toBook(bookDtoRequest)).thenReturn();
//            //When
//            when(authorRepository.findById(UUID.fromString("MOCK"))).thenReturn(Optional.ofNullable(author));
//            //Then
//        }
//    }
//    @Test
//     void getBookById() {
//    }
//
//    @Test
//    void getAllBooks() {
//    }
//
//    @Test
//    void createBook() {
//    }
//
//    @Test
//    void deleteBookById() {
//    }
//
//    @Test
//    void updateBook() {
//    }
//}