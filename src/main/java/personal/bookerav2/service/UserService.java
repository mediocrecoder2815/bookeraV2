package personal.bookerav2.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import personal.bookerav2.dto.user.UserDtoResponse;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.User;
import personal.bookerav2.entities.UserBook;
import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.UserBookRepository;
import personal.bookerav2.repository.UserRepository;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static personal.bookerav2.dto.wrappers.UserMapper.toUserDtoResponse;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;


    public UserDtoResponse getMe(Principal principal){
        User owner = findUserByUsername(principal.getName());
        Set<UserBook> userBook = findUserBooks(owner.getUserId());
        Set<Book> savedBooks = new HashSet<>(bookRepository.findAllById(
          userBook.stream()
                  .map(uB -> uB.getBookId().getBookId()).
                  collect(Collectors.toSet())
        ));
        log.debug(toUserDtoResponse(owner,savedBooks, userBook).toString());
        return toUserDtoResponse(owner,savedBooks, userBook);

    }



    private User findUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFound("User  with username " + username +" doesn't exisits!"));
    }
    private Set<UserBook> findUserBooks(UUID userId){
        return userBookRepository.findByUserId_UserId(userId);
    }
}
