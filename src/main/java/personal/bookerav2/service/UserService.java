package personal.bookerav2.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.bookerav2.dto.reviews.ReviewDtoResponse;
import personal.bookerav2.dto.user.UserBookDtoRequest;
import personal.bookerav2.dto.user.UserDtoResponse;
import personal.bookerav2.dto.wrappers.ReviewMapper;
import personal.bookerav2.entities.*;
import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.ReviewRepository;
import personal.bookerav2.repository.UserBookRepository;
import personal.bookerav2.repository.UserRepository;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static personal.bookerav2.dto.wrappers.UserMapper.toUserDtoResponse;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    public UserDtoResponse getMe(Principal principal){
        User owner = findUserByUsername(principal.getName());
        Set<UserBook> userBook = findUserBooks(owner.getUserId());
        Set<Book> savedBooks = new HashSet<>(bookRepository.findAllById(
          userBook.stream()
                  .map(uB -> uB.getBookId().getBookId()).
                  collect(Collectors.toSet())
        ));
        Set<Review> reviews = reviewRepository.findByUser(owner);
        Set<ReviewDtoResponse> reviewDtos = reviews.stream().
                map(ReviewMapper::toReviewDtoResponse).
                collect(Collectors.toSet());
        return toUserDtoResponse(owner,savedBooks, userBook, reviewDtos);
    }

    @Transactional
    public UserDtoResponse addBook(Principal principal, UserBookDtoRequest userBook) {
        User owner = findUserByUsername(principal.getName());
        Book bookToAdd = findBookById(userBook.bookId());
        Set<UserBook> ub = findUserBooks(owner.getUserId());
        for (UserBook book : ub ){
            if (book.getBookId().getBookId().equals(userBook.bookId())){
                throw new UnsupportedOperationException("Already in the shelf");
            }
        }
        UserBook newUserBook = new UserBook();
        newUserBook.setUserId(owner);
        newUserBook.setBookId(bookToAdd);
        newUserBook.setBookStatus(userBook.statusId());
        newUserBook.setUserBookId(new UserBookId(owner.getUserId(), userBook.bookId()));
        userBookRepository.save(newUserBook);
        return getMe(principal);
    }

    public UserDtoResponse updateStatus(Principal principal, UserBookDtoRequest bookUpdate){
        if (0 > bookUpdate.statusId() || bookUpdate.statusId() > 3){
            throw new UnsupportedOperationException("Wrong status id only (1-3) supported");
        }
        User owner = findUserByUsername(principal.getName());
        Book bookToChange = findBookById(bookUpdate.bookId());
        Set<UserBook> userBook = userBookRepository.findByUserId_UserId(owner.getUserId());
        Optional<UserBook> ub = userBook.stream()
                .filter(userBook1 -> userBook1.getBookId().equals(bookToChange))
                .findAny();
        if(ub.isEmpty()){
            throw new UnsupportedOperationException("Should use only with existing items, try add method");
        }
        ub.get().setBookStatus(bookUpdate.statusId());
        userBookRepository.save(ub.get());
        return getMe(principal);
    }


    private User findUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFound("User  with username " + username +" doesn't exisits!"));
    }
    private Book findBookById(Long bookId){
        return bookRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFound("Book with id " + bookId + " doesn't exists"));
    }
    private Set<UserBook> findUserBooks(UUID userId){
        return userBookRepository.findByUserId_UserId(userId);
    }

}
