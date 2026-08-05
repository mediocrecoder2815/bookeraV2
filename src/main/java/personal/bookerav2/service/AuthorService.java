package personal.bookerav2.service;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.bookerav2.dto.authors.AuthorDtoAll;
import personal.bookerav2.dto.authors.AuthorDtoRequest;
import personal.bookerav2.dto.authors.AuthorDtoResponse;
import personal.bookerav2.dto.wrappers.AuthorMapper;
import personal.bookerav2.entities.Author;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.User;
import personal.bookerav2.entities.enums.CountryCode;
import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.AuthorRepository;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.UserRepository;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public AuthorDtoResponse getAuthorById(Long id){
        Author authorToFind = findById(id);
        return AuthorMapper.toResponseDto(authorToFind);
    }
    public Page<AuthorDtoAll> getAllAuthors(Pageable pageable){
        return authorRepository.findAll(pageable)
                .map(AuthorMapper::toAuthorDtoAll);
    }

    @Transactional
    public void deleteAuthorById(Long id) {
        Author author = findById(id);

        for (Book book : bookRepository.findByAuthors_AuthorId(id)) {
            book.getAuthors().remove(author);
            if (book.getAuthors().isEmpty()) {
                deleteBookAndReferences(book);
            } else {
                bookRepository.save(book);
            }
        }
        authorRepository.delete(author);
    }

    private void deleteBookAndReferences(Book book) {
        for (User user : userRepository.findByBooks_bookId(book.getBookId())) {
            user.getBooks().remove(book);
        }
        bookRepository.delete(book);
    }

    @Transactional
    public AuthorDtoResponse createAuthor(AuthorDtoRequest a){
        Author newAuthor = new Author();
        newAuthor.setName(a.name());
        newAuthor.setSurname(a.surname());
        newAuthor.setPictureUrl(a.pictureUrl());
        newAuthor.setDescription(a.description());
        newAuthor.setCountry(CountryCode.convert(a.countryCode()));
        newAuthor.setDateOfBirth(a.dateOfBirth());
        return AuthorMapper.toResponseDto(authorRepository.save(newAuthor));
    }

    @Transactional
    public AuthorDtoResponse updateAuthor(AuthorDtoRequest a, Long id){
        Author authorToUpdate = findById(id);
        authorToUpdate.setName(a.name());
        authorToUpdate.setSurname(a.surname());
        authorToUpdate.setDescription(a.description());
        authorToUpdate.setDateOfBirth(a.dateOfBirth());
        authorToUpdate.setCountry(CountryCode.convert(a.countryCode()));
        return AuthorMapper.toResponseDto(authorRepository.save(authorToUpdate));
    }

    private Author findById(long id){
        return authorRepository.findById(id).orElseThrow
                (() -> new ResourceNotFound("Author with id :" + id + " doesn't exists"));
    }
}
