package personal.bookerav2.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.bookerav2.dto.books.BookDtoAll;
import personal.bookerav2.dto.books.BookDtoRequest;
import personal.bookerav2.dto.books.BookDtoResponse;
import personal.bookerav2.service.BookService;

import java.util.List;
import java.util.UUID;

@RestController("/api/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDtoAll>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookDtoResponse> getBookById(@PathVariable UUID id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }
    @PostMapping
    public ResponseEntity<BookDtoResponse> addBook(BookDtoRequest book){
        return ResponseEntity.ok(bookService.createBook(book));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteBookById(@PathVariable UUID id){
        bookService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<BookDtoResponse> updateBook(@PathVariable UUID id, @RequestBody BookDtoRequest newBook){
        return ResponseEntity.ok(bookService.updateBook(newBook, id));
    }

}
