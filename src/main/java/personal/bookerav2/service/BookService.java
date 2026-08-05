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
import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.AuthorRepository;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.CategoryRepository;
import personal.bookerav2.repository.UserRepository;

import java.util.*;


@Service
@Slf4j
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public BookDtoResponse getBookById(Long id){
        Book bookToFind = findById(id);
        return BookMapper.toResponseDto(bookToFind);
    }

    public Page<BookDtoAll> getAllBooks(Pageable page){
        Page<Book> books = bookRepository.findAll(page);
        log.info("Retrieving {} books, page {}",page.getPageNumber(), page.getOffset());
        return books.map(BookMapper::toAllBookDto);
    }

    @Transactional
    public BookDtoResponse createBook(BookDtoRequest book){
        Book newBook = BookMapper.toBook(book);
        Author author = authorRepository.findById(book.authorId())
                .orElseThrow(() -> new ResourceNotFound("Author with id " + book.authorId() + " not found!" ));
        log.info("Trying to create book: {}", newBook);
        if(book.categoriesId().isPresent()){
            List<Category> categories = new ArrayList<>();
            for(Integer id : book.categoriesId().get()){
                categories.add(categoryRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFound("Category with id " + id + " not found")
                ));
            }
            newBook.setCategories(new HashSet<>(categories));
        }
        newBook.getAuthors().add(author);
        bookRepository.save(newBook);
        log.info("Created book: {}", newBook);
        return BookMapper.toResponseDto(newBook);
    }

    public void deleteBookById(Long id){
        Book bookToDelete = findById(id);
        log.info("Deleting book with id: {}", id);
        authorRepository.findByBooks_BookId(id).forEach(
                a -> a.getBooks().remove(bookToDelete)
        );
        userRepository.findByBooks_bookId(id).forEach(
                u -> u.getBooks().remove(bookToDelete)
        );

        bookRepository.delete(bookToDelete);
        log.info("Book with id {} | was deleted", id);
    }



    @Transactional
    public BookDtoResponse updateBook(BookDtoRequest bookRequest, Long bookId){
        Book bookToUpdate = findById(bookId);
        bookToUpdate.setName(bookRequest.name());
        bookToUpdate.setIsbn(bookRequest.isbn());
        bookToUpdate.setDescription(bookRequest.description());
        bookToUpdate.setTotalPages(bookRequest.totalPages());
        bookToUpdate.setDateOfPublish(bookRequest.dateOfPublish());
        bookRepository.save(bookToUpdate);
        return BookMapper.toResponseDto(bookToUpdate);
    }

    private Book findById(long id){
        return bookRepository.findById(id).
                orElseThrow(() -> new ResourceNotFound("Book with id " + id + " not found!"));

    }
}
