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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.bookerav2.dto.authors.AuthorDtoAll;
import personal.bookerav2.dto.authors.AuthorDtoRequest;
import personal.bookerav2.dto.authors.AuthorDtoResponse;
import personal.bookerav2.service.AuthorService;
import personal.bookerav2.util.PageRequestFactory;

import java.util.Set;

@RestController
@RequestMapping("/api/authors")
@AllArgsConstructor
@Tag(name = "Authors", description = "Endpoints for managing authors")
public class AuthorController {
    private final AuthorService authorService;

    private static final Set<String> SORTABLE_FIELDS = Set.of("authorId", "name", "surname", "dateOfBirth", "country");

    @Operation(summary = "Get all authors",
            description = "Returns a paginated list of authors.")
    @ApiResponse(responseCode = "200", description = "Paginated list of authors",
            content = @Content(schema = @Schema(implementation = AuthorDtoAll.class)))
    @GetMapping
    public ResponseEntity<Page<AuthorDtoAll>> getAllAuthors(
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "authorId") String sortBy,
            @Parameter(description = "Sort ascending if true, descending if false") @RequestParam(defaultValue = "true") boolean ascending)
    {
        Pageable pageable = PageRequestFactory.from(page, size, sortBy, ascending, SORTABLE_FIELDS, "authorId");
        return ResponseEntity.ok(authorService.getAllAuthors(pageable));
    }

    @Operation(summary = "Get an author by ID")
    @Parameter(name = "id", description = "Author ID")
    @ApiResponse(responseCode = "200", description = "Author found",
            content = @Content(schema = @Schema(implementation = AuthorDtoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Author not found")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDtoResponse> getAuthorById(@PathVariable Long id){
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @Operation(summary = "Create a new author")
    @ApiResponse(responseCode = "200", description = "Author created",
            content = @Content(schema = @Schema(implementation = AuthorDtoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @PostMapping
    public ResponseEntity<AuthorDtoResponse> createAuthor(@RequestBody @Valid AuthorDtoRequest newAuthor){
        return ResponseEntity.ok(authorService.createAuthor(newAuthor));
    }

    @Operation(summary = "Delete an author by ID")
    @Parameter(name = "id", description = "Author ID")
    @ApiResponse(responseCode = "200", description = "Author deleted")
    @ApiResponse(responseCode = "404", description = "Author not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id){
        authorService.deleteAuthorById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Update an author by ID")
    @Parameter(name = "id", description = "Author ID")
    @ApiResponse(responseCode = "200", description = "Author updated",
            content = @Content(schema = @Schema(implementation = AuthorDtoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Author not found")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDtoResponse> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDtoRequest author){
        return ResponseEntity.ok(authorService.updateAuthor(author, id));
    }



}
