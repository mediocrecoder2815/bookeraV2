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
import personal.bookerav2.repository.AuthorRepository;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.CategoryRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.rmi.server.LogStream.log;

@Service
@Slf4j
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public BookDtoResponse getBookById(UUID id){
        Book bookToFind = bookRepository.findById(id).orElseThrow();
        return BookMapper.toResponseDto(bookToFind);
    }

    public Page<BookDtoAll> getAllBooks(Pageable page){
        Page<Book> books = bookRepository.findAll(page);
        return books.map(BookMapper::toAllBookDto);
    }

    public BookDtoResponse createBook(BookDtoRequest book){
        Book newBook = BookMapper.toBook(book);
        Author author = authorRepository.findById(book.authorId()).orElseThrow();
        if(book.categoriesId().isPresent()){
            List<Category> categories = new ArrayList<>();
            for(Long l : book.categoriesId().get()){
                categories.add(categoryRepository.findById(l).orElseThrow());
            }
            newBook.setCategories(new HashSet<>(categories));
        }
        newBook.getAuthors().add(author);
        bookRepository.save(newBook);
        return BookMapper.toResponseDto(newBook);
    }

    public void deleteBookById(UUID id){
        Book bookToDelete = bookRepository.findById(id).orElseThrow();
        bookRepository.delete(bookToDelete);
        log("Book was deleted");
    }

    public BookDtoResponse updateBook(BookDtoRequest bookRequest, UUID bookId){
        Book bookToUpdate = bookRepository.findById(bookId).orElseThrow();
        bookToUpdate.setName(bookRequest.name());
        bookToUpdate.setIsbn(bookRequest.isbn());
        bookToUpdate.setDescription(bookRequest.description());
        bookToUpdate.setTotalPages(bookRequest.totalPages());
        bookToUpdate.setDateOfPublish(bookRequest.dateOfPublish());
        bookRepository.save(bookToUpdate);
        return BookMapper.toResponseDto(bookToUpdate);
    }

}
