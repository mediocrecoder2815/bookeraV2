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
import personal.bookerav2.dto.authors.AuthorDtoAll;
import personal.bookerav2.dto.authors.AuthorDtoRequest;
import personal.bookerav2.dto.authors.AuthorDtoResponse;
import personal.bookerav2.dto.wrappers.AuthorMapper;
import personal.bookerav2.entities.Author;
import personal.bookerav2.entities.enums.CountryCode;
import personal.bookerav2.repository.AuthorRepository;
import personal.bookerav2.repository.BookRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import personal.bookerav2.exceptions.ResourceNotFound;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthorService unit tests")
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorDtoRequest authorDtoRequest;

    @BeforeEach
    void setUp() {
        author = Author.builder()
                .authorId(1L)
                .name("John")
                .surname("Doe")
                .description("An author")
                .country(CountryCode.US)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .books(new HashSet<>())
                .build();

        authorDtoRequest = new AuthorDtoRequest(
                "John",
                "Doe",
                "An author",
                null,
                "US",
                LocalDate.of(1990, 1, 1)
        );
    }

    @Nested
    @DisplayName("getAuthorById")
    class GetAuthorById {

        @Test
        void shouldReturnAuthorWhenFound() {
            when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

            try (var mapper = mockStatic(AuthorMapper.class)) {
                AuthorDtoResponse expected = new AuthorDtoResponse(
                        1L, "John", "Doe", "An author", CountryCode.US,
                        LocalDate.of(1990, 1, 1), null, new HashSet<>()
                );
                mapper.when(() -> AuthorMapper.toResponseDto(author)).thenReturn(expected);

                AuthorDtoResponse result = authorService.getAuthorById(1L);

                assertNotNull(result);
                assertEquals(expected, result);
            }
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(authorRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> authorService.getAuthorById(99L));
        }
    }

    @Nested
    @DisplayName("getAllAuthors")
    class GetAllAuthors {

        @Test
        void shouldReturnPageOfAuthors() {
            Page<Author> authorPage = new PageImpl<>(List.of(author));
            PageRequest pageable = PageRequest.of(0, 10);
            when(authorRepository.findAll(pageable)).thenReturn(authorPage);

            Page<AuthorDtoAll> result = authorService.getAllAuthors(pageable);

            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            assertEquals("John", result.getContent().getFirst().name());
        }
    }

    @Nested
    @DisplayName("createAuthor")
    class CreateAuthor {

        @Test
        void shouldCreateAuthorSuccessfully() {
            when(authorRepository.save(any(Author.class))).thenReturn(author);

            try (var mapper = mockStatic(AuthorMapper.class)) {
                Author newAuthor = Author.builder()
                        .name("John")
                        .surname("Doe")
                        .description("An author")
                        .country(CountryCode.US)
                        .dateOfBirth(LocalDate.of(1990, 1, 1))
                        .build();
                mapper.when(() -> AuthorMapper.toAuthor(authorDtoRequest)).thenReturn(newAuthor);

                AuthorDtoResponse expected = new AuthorDtoResponse(
                        1L, "John", "Doe", "An author", CountryCode.US,
                        LocalDate.of(1990, 1, 1), null, new HashSet<>()
                );
                mapper.when(() -> AuthorMapper.toResponseDto(author)).thenReturn(expected);

                AuthorDtoResponse result = authorService.createAuthor(authorDtoRequest);

                assertNotNull(result);
                assertEquals(expected, result);
                verify(authorRepository).save(any(Author.class));
            }
        }
    }

    @Nested
    @DisplayName("updateAuthor")
    class UpdateAuthor {

        @Test
        void shouldUpdateAuthorSuccessfully() {
            when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
            when(authorRepository.save(any(Author.class))).thenReturn(author);

            try (var mapper = mockStatic(AuthorMapper.class)) {
                AuthorDtoResponse expected = new AuthorDtoResponse(
                        1L, "John", "Doe", "An author", CountryCode.US,
                        LocalDate.of(1990, 1, 1), null, new HashSet<>()
                );
                mapper.when(() -> AuthorMapper.toResponseDto(author)).thenReturn(expected);

                AuthorDtoResponse result = authorService.updateAuthor(authorDtoRequest, 1L);

                assertNotNull(result);
                assertEquals(expected, result);
                verify(authorRepository).findById(1L);
                verify(authorRepository).save(any(Author.class));
            }
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(authorRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class,
                    () -> authorService.updateAuthor(authorDtoRequest, 99L));
        }
    }

    @Nested
    @DisplayName("deleteAuthorById")
    class DeleteAuthorById {

        @Test
        void shouldDeleteAuthorSuccessfully() {
            when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

            authorService.deleteAuthorById(1L);

            verify(authorRepository).findById(1L);
            verify(authorRepository).delete(author);
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(authorRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> authorService.deleteAuthorById(99L));
        }
    }
}
