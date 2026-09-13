package personal.bookerav2.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import personal.bookerav2.dto.authors.AuthorDtoAll;
import personal.bookerav2.dto.authors.AuthorDtoRequest;
import personal.bookerav2.dto.authors.AuthorDtoResponse;
import personal.bookerav2.dto.wrappers.AuthorMapper;
import personal.bookerav2.entities.Author;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.Role;
import personal.bookerav2.entities.User;
import personal.bookerav2.entities.enums.CountryCode;
import personal.bookerav2.exceptions.InvalidCredentialsException;
import personal.bookerav2.exceptions.InvalidFileException;
import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.AuthorRepository;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    public AuthorDtoResponse getAuthorById(Long id){
        Author authorToFind = findById(id);
        return AuthorMapper.toResponseDto(authorToFind);
    }
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

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
        // TODO: when deleting a book it must also be deleted from user_books
        bookRepository.delete(book);
    }

    @Transactional
    public AuthorDtoResponse createAuthor(AuthorDtoRequest a){
        Author newAuthor = AuthorMapper.toAuthor(a);
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

    public AuthorDtoResponse uploadPicture(Long authorId, MultipartFile file, Principal principal) throws IOException {
        if(file.isEmpty() || file == null){
            throw new InvalidFileException("No file was found");
        }
//        User requester = findUserByName(principal.getName());
//        if(!this.isAdmin(requester)){
//            throw new InvalidCredentialsException("Wrong user's role");
//        }
        Author author = findById(authorId);

        Path authorDir = Path.of(uploadDir, "authors").toAbsolutePath();
        Files.createDirectories(authorDir);
        if (author.getPictureUrl() != null) {
            Files.deleteIfExists(resolveStoredFile(authorDir, author.getPictureUrl()));
        }
        UUID pictureId = UUID.randomUUID();
        String extension = switch (file.getContentType()) {
            case "image/png"  -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif"  -> ".gif";
            default           -> ".jpg";
        };
        String filename = pictureId + extension;
        String ct = file.getContentType();
        if (ct == null || !List.of("image/jpeg","image/png","image/webp","image/gif").contains(ct)) {
            throw new InvalidFileException("Unsupported image type: " + ct);
        }
        file.transferTo(authorDir.resolve(filename).toFile());
        author.setPictureUrl("/uploads/author/" + filename);
        authorRepository.save(author);
        return getAuthorById(authorId);
    }

    private Path resolveStoredFile(Path authorDir, String storedUrl) {
        String filename = storedUrl.substring(storedUrl.lastIndexOf('/') + 1);
        return authorDir.resolve(filename);
    }

    private User findUserByName(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFound("User was not found"));
    }
    private boolean isAdmin(User user){
        List<Role> roles = user.getRoles()
                .stream()
                .filter(r -> r.getRoleName()
                        .equals("ADMIN")).toList();
        log.info(roles.toString());
        return roles.isEmpty();
    }
    private Author findById(long id){
        return authorRepository.findById(id).orElseThrow
                (() -> new ResourceNotFound("Author with id :" + id + " doesn't exists"));
    }
}
