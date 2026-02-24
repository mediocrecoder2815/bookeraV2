package personal.bookerav2.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import personal.bookerav2.dto.books.BookDtoResponse;
import personal.bookerav2.repository.BookRepository;

import java.util.Set;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Set<BookDtoResponse> getBookById(){

    }
}
