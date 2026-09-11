package personal.bookerav2.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.bookerav2.dto.books.BookDtoAll;
import personal.bookerav2.dto.books.BookDtoRequest;
import personal.bookerav2.dto.books.BookDtoResponse;
import personal.bookerav2.dto.wrappers.BookMapper;
import personal.bookerav2.entities.Author;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.Category;
import personal.bookerav2.entities.Review;
import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.AuthorRepository;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.CategoryRepository;
import personal.bookerav2.repository.ReviewRepository;


import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

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

    public void deleteBookById(Long id) {
        Book bookToDelete = findBookById(id);
        log.info("Deleting book with id: {}", id);

        authorRepository.findByBooks_BookId(id).forEach(
                author -> author.getBooks().remove(bookToDelete)
        );
        // TODO: when deleting a book it must also be deleted from user_books

        bookRepository.delete(bookToDelete);
        log.info("Book with id {} was deleted", id);
    }

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

    private Double getAverageRating(Long bookId) {
        if (bookId == null) {
            return 0.0;
        }
        Double avg = reviewRepository.findAverageRatingByBookId(bookId);
        return avg != null ? avg : 0.0;
    }

    private Book findBookById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Book with id " + id + " not found!"));
    }
    private Book syncReviews(Long bookId){
        Book book = findBookById(bookId);
        int reviewCount = reviewRepository.findReviewCountByBookId(bookId);
        BigDecimal avg = new BigDecimal(reviewRepository.findAverageRatingByBookId(book.getBookId()));
        book.setReviewCount(reviewCount);
        book.setAvgRating(avg);
        return bookRepository.save(book);
    }
    private Author findAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Author with id " + id + " not found!"));
    }
}
