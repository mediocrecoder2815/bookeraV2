package personal.bookerav2.controller;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.bookerav2.dto.authors.AuthorDtoAll;
import personal.bookerav2.dto.authors.AuthorDtoRequest;
import personal.bookerav2.dto.authors.AuthorDtoResponse;
import personal.bookerav2.service.AuthorService;

@RestController
@RequestMapping("/api/authors")
@AllArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/all")
    public ResponseEntity<Page<AuthorDtoAll>> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending)
    {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDtoResponse> getAuthorById(@PathVariable Integer id){
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorDtoResponse> createAuthor(@RequestBody AuthorDtoRequest newAuthor){
        return ResponseEntity.ok(authorService.createAuthor(newAuthor));
    }

    @DeleteMapping("/{id}")
    public boolean deleteAuthor(@PathVariable Integer id){
        authorService.deleteAuthorById(id);
        return true;
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDtoResponse> updateAuthor(@PathVariable Integer id, @RequestBody AuthorDtoRequest author){
        return ResponseEntity.ok(authorService.updateAuthor(author, id));
    }



}
