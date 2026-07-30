package personal.bookerav2.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

import java.util.*;


@Service
@Slf4j
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public BookDtoResponse getBookById(Integer id){
        Book bookToFind = findById(id);
        return BookMapper.toResponseDto(bookToFind);
    }

    public Page<BookDtoAll> getAllBooks(Pageable page){
        Page<Book> books = bookRepository.findAll(page);
        log.info("Retrieving {} books, page {}",page.getPageNumber(), page.getOffset());
        return books.map(BookMapper::toAllBookDto);
    }

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

    public void deleteBookById(Integer id){

        Book bookToDelete = findById(id);
        log.info("Deleting book with id: {}", id);
        bookRepository.delete(bookToDelete);
        log.info("Book with id {} | was deleted", id);
    }

    public BookDtoResponse updateBook(BookDtoRequest bookRequest, Integer bookId){
        Book bookToUpdate = findById(bookId);
        bookToUpdate.setName(bookRequest.name());
        bookToUpdate.setIsbn(bookRequest.isbn());
        bookToUpdate.setDescription(bookRequest.description());
        bookToUpdate.setTotalPages(bookRequest.totalPages());
        bookToUpdate.setDateOfPublish(bookRequest.dateOfPublish());
        bookRepository.save(bookToUpdate);
        return BookMapper.toResponseDto(bookToUpdate);
    }

    private Book findById(int id){
        return bookRepository.findById(id).
                orElseThrow(() -> new ResourceNotFound("Book with id " + id + " not found!"));

    }
}
