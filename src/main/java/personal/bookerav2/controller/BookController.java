package personal.bookerav2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Books", description = "Endpoints for managing books")
public class BookController {
    private final BookService bookService;

    private static final Set<String> SORTABLE_FIELDS = Set.of("bookId", "name", "totalPages", "dateOfPublish");

    @Operation(summary = "Get all books",
            description = "Returns a paginated list of books.")
    @ApiResponse(responseCode = "200", description = "Paginated list of books",
            content = @Content(schema = @Schema(implementation = BookDtoAll.class)))
    @GetMapping
    public ResponseEntity<Page<BookDtoAll>> getAllBooks(
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "bookId") String sortBy,
            @Parameter(description = "Sort ascending if true, descending if false") @RequestParam(defaultValue = "true") boolean ascending)
    {
        Pageable pageable = PageRequestFactory.from(page, size, sortBy, ascending, SORTABLE_FIELDS, "bookId");
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @Operation(summary = "Get a book by ID")
    @Parameter(name = "id", description = "Book ID")
    @ApiResponse(responseCode = "200", description = "Book found",
            content = @Content(schema = @Schema(implementation = BookDtoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Book not found")
    @GetMapping("/{id}")
    public ResponseEntity<BookDtoResponse> getBookById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(summary = "Add a new book")
    @ApiResponse(responseCode = "200", description = "Book created",
            content = @Content(schema = @Schema(implementation = BookDtoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @PostMapping
    public ResponseEntity<BookDtoResponse> addBook(@RequestBody @Valid BookDtoRequest book){
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @Operation(summary = "Delete a book by ID")
    @Parameter(name = "id", description = "Book ID")
    @ApiResponse(responseCode = "200", description = "Book deleted")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id){
        bookService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update a book by ID")
    @Parameter(name = "id", description = "Book ID")
    @ApiResponse(responseCode = "200", description = "Book updated",
            content = @Content(schema = @Schema(implementation = BookDtoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Book not found")
    @PutMapping("/{id}")
    public ResponseEntity<BookDtoResponse> updateBook(@PathVariable Long id, @RequestBody @Valid BookDtoRequest newBook){
        return ResponseEntity.ok(bookService.updateBook(newBook, id));
    }

}
