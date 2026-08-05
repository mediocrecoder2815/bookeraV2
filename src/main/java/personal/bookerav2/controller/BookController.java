package personal.bookerav2.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.bookerav2.dto.books.BookDtoAll;
import personal.bookerav2.dto.books.BookDtoRequest;
import personal.bookerav2.dto.books.BookDtoResponse;
import personal.bookerav2.service.BookService;
import personal.bookerav2.util.PageRequestFactory;

import java.util.Set;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    private static final Set<String> SORTABLE_FIELDS = Set.of("bookId", "name", "totalPages", "dateOfPublish");

    @GetMapping
    public ResponseEntity<Page<BookDtoAll>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookId") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending)
    {
        Pageable pageable = PageRequestFactory.from(page, size, sortBy, ascending, SORTABLE_FIELDS, "bookId");
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDtoResponse> getBookById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }
    @PostMapping
    public ResponseEntity<BookDtoResponse> addBook(@RequestBody @Valid BookDtoRequest book){
        return ResponseEntity.ok(bookService.createBook(book));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id){
        bookService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<BookDtoResponse> updateBook(@PathVariable Long id, @RequestBody @Valid BookDtoRequest newBook){
        return ResponseEntity.ok(bookService.updateBook(newBook, id));
    }

}
