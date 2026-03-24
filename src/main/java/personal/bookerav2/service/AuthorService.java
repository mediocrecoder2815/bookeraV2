package personal.bookerav2.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import personal.bookerav2.dto.authors.AuthorDtoAll;
import personal.bookerav2.dto.authors.AuthorDtoRequest;
import personal.bookerav2.dto.authors.AuthorDtoResponse;
import personal.bookerav2.dto.wrappers.AuthorMapper;
import personal.bookerav2.entities.Author;
import personal.bookerav2.repository.AuthorRepository;
import personal.bookerav2.repository.BookRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;


    public AuthorDtoResponse getAuthorById(UUID id){
        Author authorToFind = authorRepository.findById(id).orElseThrow();
        return AuthorMapper.toResponseDto(authorToFind);
    }
    public List<AuthorDtoAll> getAllAuthors(){
        List<Author> allAuthors = authorRepository.findAll();
        return allAuthors.
                stream().
                map(AuthorMapper::toAuthorDtoAll).
                collect(Collectors.toList());
    }
    public void deleteAuthorById(UUID id){
        Author author = authorRepository.findById(id).orElseThrow();
        authorRepository.delete(author);
    }

    public AuthorDtoResponse createAuthor(AuthorDtoRequest a){
        Author newAuthor = new Author();
        newAuthor.setName(a.name());
        newAuthor.setSurname(a.surname());
        newAuthor.setDescription(a.description());
        newAuthor.setCountry(a.countryCode());
        newAuthor.setDateOfBirth(a.dateOfBirth());


        return AuthorMapper.toResponseDto(authorRepository.save(newAuthor));
    }
    public AuthorDtoResponse updateAuthor(AuthorDtoRequest a, UUID id){
        Author authorToUpdate = authorRepository.findById(id).orElseThrow();
        authorToUpdate.setName(a.name());
        authorToUpdate.setSurname(a.surname());
        authorToUpdate.setDescription(a.description());
        authorToUpdate.setDateOfBirth(a.dateOfBirth());
        authorToUpdate.setCountry(a.countryCode());
        return AuthorMapper.toResponseDto(authorRepository.save(authorToUpdate));
    }


}
