package personal.bookerav2.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
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
public class AuthorController {
    private final AuthorService authorService;

    private static final Set<String> SORTABLE_FIELDS = Set.of("authorId", "name", "surname", "dateOfBirth", "country");

    @GetMapping
    public ResponseEntity<Page<AuthorDtoAll>> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "authorId") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending)
    {
        Pageable pageable = PageRequestFactory.from(page, size, sortBy, ascending, SORTABLE_FIELDS, "authorId");
        return ResponseEntity.ok(authorService.getAllAuthors(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDtoResponse> getAuthorById(@PathVariable Long id){
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorDtoResponse> createAuthor(@RequestBody @Valid AuthorDtoRequest newAuthor){
        return ResponseEntity.ok(authorService.createAuthor(newAuthor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id){
        authorService.deleteAuthorById(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDtoResponse> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDtoRequest author){
        return ResponseEntity.ok(authorService.updateAuthor(author, id));
    }



}
