package personal.bookerav2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import personal.bookerav2.dto.books.BookDtoAll;
import personal.bookerav2.dto.books.BookDtoRequest;
import personal.bookerav2.dto.books.BookDtoResponse;
import personal.bookerav2.dto.wrappers.BookMapper;
import personal.bookerav2.entities.Author;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.Category;
import personal.bookerav2.entities.Review;
import personal.bookerav2.exceptions.InvalidFileException;
import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.AuthorRepository;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.CategoryRepository;
import personal.bookerav2.repository.ReviewRepository;
import personal.bookerav2.repository.UserBookRepository;


import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final UserBookRepository userBookRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Cacheable(value = "BOOK_CACHE", key = "#id")
    public BookDtoResponse getBookById(Long id) {
        Book book = syncReviews(id);
        int[] ratings = new int[5];

        Set<Review> reviews = reviewRepository.findByBook(book);
        for(Review r : reviews){
            if(r.getRating() >= 1 && r.getRating()<=5){
                ratings[r.getRating() - 1] +=1;
            }
        }

        return BookMapper.toResponseDto(book, book.getAvgRating().
                toBigInteger()
                .doubleValue(),
                ratings);
    }

    public Page<BookDtoAll> getAllBooks(Pageable page) {
        Page<Book> books = bookRepository.findAll(page);
        log.info("Retrieving {} books, page {}", page.getPageNumber(), page.getOffset());
        return books.map(BookMapper::toAllBookDto);
    }

    @CachePut(value = "BOOK_CACHE", key = "#result.bookId()")
    @Transactional
    public BookDtoResponse createBook(BookDtoRequest bookRequest) {
        Book newBook = BookMapper.toBook(bookRequest);
        Author author = findAuthorById(bookRequest.authorId());
        log.info("Trying to create book: {}", newBook);

        if (bookRequest.categoriesId() != null) {
            Set<Category> categories = bookRequest.categoriesId().stream()
                    .map(id -> categoryRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFound("Category with id " + id + " not found")))
                    .collect(Collectors.toSet());
            newBook.setCategories(categories);
        }

        newBook.getAuthors().add(author);
        Book savedBook = bookRepository.save(newBook);
        log.info("Created book: {}", savedBook);

        Double avgRating = getAverageRating(savedBook.getBookId());
        return BookMapper.toResponseDto(savedBook, avgRating, new int[5]);
    }


    @CacheEvict(value = "BOOK_CACHE", key = "#id")
    @Transactional
    public void deleteBookById(Long id) {
        Book bookToDelete = findBookById(id);
        log.info("Deleting book with id: {}", id);

        userBookRepository.deleteByBookId_BookId(id);
        bookRepository.delete(bookToDelete);
        log.info("Book with id {} was deleted", id);
    }

    @CachePut(value = "BOOK_CACHE", key = "#result.bookId()")
    @Transactional
    public BookDtoResponse updateBook(BookDtoRequest bookRequest, Long bookId) {
        Book bookToUpdate = findBookById(bookId);
        bookToUpdate.setName(bookRequest.name());
        bookToUpdate.setIsbn(bookRequest.isbn());
        bookToUpdate.setDescription(bookRequest.description());
        bookToUpdate.setTotalPages(bookRequest.totalPages());
        bookToUpdate.setDateOfPublish(bookRequest.dateOfPublish());

        Book savedBook = bookRepository.save(bookToUpdate);
        Double avgRating = getAverageRating(savedBook.getBookId());
        return BookMapper.toResponseDto(savedBook, avgRating, new int[5]);
    }

    public BookDtoResponse updateBookImage(Long bookId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("No file was found");
        }
        Book book = findBookById(bookId);

        Path booksDir = Path.of(uploadDir, "books").toAbsolutePath();
        Files.createDirectories(booksDir);

        if (book.getPictureUrl() != null) {
            Files.deleteIfExists(resolveStoredFile(booksDir, book.getPictureUrl()));
        }
        UUID pictureId = UUID.randomUUID();
        String extension = switch (file.getContentType()) {
            case "image/png"  -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif"  -> ".gif";
            default           -> ".jpg";
        };
        String filename = pictureId + extension;
        String ct = file.getContentType();
        if (ct == null || !List.of("image/jpeg","image/png","image/webp","image/gif").contains(ct)) {
            throw new InvalidFileException("Unsupported image type: " + ct);
        }
        file.transferTo(booksDir.resolve(filename).toFile());
        book.setPictureUrl("/uploads/books/" + filename);
        bookRepository.save(book);
        log.debug("SAVED FILE TO {}", uploadDir);
        return getBookById(bookId);
    }

    private Double getAverageRating(Long bookId) {
        if (bookId == null) {
            return 0.0;
        }
        Optional<Double> avg = reviewRepository.findAverageRatingByBookId(bookId);
        return avg.orElse(0.0);
    }
    private Book findBookById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Book with id " + id + " not found!"));
    }
    private Book syncReviews(Long bookId){
        Book book = findBookById(bookId);
        int reviewCount = reviewRepository.findReviewCountByBookId(bookId);
        BigDecimal avg = new BigDecimal(reviewRepository.findAverageRatingByBookId(book.getBookId()).orElse(0.0));
        book.setReviewCount(reviewCount);
        book.setAvgRating(avg);
        return bookRepository.save(book);
    }

    private Author findAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Author with id " + id + " not found!"));
    }

    private Path resolveStoredFile(Path booksDir, String storedUrl) {
        String filename = storedUrl.substring(storedUrl.lastIndexOf('/') + 1);
        return booksDir.resolve(filename);
    }
}
