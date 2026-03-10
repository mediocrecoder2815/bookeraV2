package personal.bookerav2.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.repository.ReviewRepository;
import personal.bookerav2.repository.UserRepository;

@Service
@AllArgsConstructor
public class ReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
}
