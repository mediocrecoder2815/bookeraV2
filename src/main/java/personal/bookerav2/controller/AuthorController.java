package personal.bookerav2.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.bookerav2.dto.authors.AuthorDtoAll;
import personal.bookerav2.dto.authors.AuthorDtoRequest;
import personal.bookerav2.dto.authors.AuthorDtoResponse;
import personal.bookerav2.service.AuthorService;

import java.util.List;
import java.util.UUID;
/*
    TO-DO
    - [ ] add exceptions \ mb return Optional in service ?
    - [ ] improve delete method
 */
@RestController("/api/authors")
@AllArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDtoAll>> getAllAuthors(){
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDtoResponse> getAuthorById(@PathVariable UUID id){
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorDtoResponse> createAuthor(@RequestBody AuthorDtoRequest newAuthor){
        return ResponseEntity.ok(authorService.createAuthor(newAuthor));
    }

    @DeleteMapping("/{id}")
    public boolean deleteAuthor(@PathVariable UUID id){
        authorService.deleteAuthorById(id);
        return true;
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDtoResponse> updateAuthor(@PathVariable UUID id, @RequestBody AuthorDtoRequest author){
        return ResponseEntity.ok(authorService.updateAuthor(author, id));
    }



}
