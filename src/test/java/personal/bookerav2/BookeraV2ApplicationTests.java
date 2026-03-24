package personal.bookerav2;

import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import personal.bookerav2.dto.books.BookDtoRequest;
import personal.bookerav2.entities.Book;
import personal.bookerav2.repository.BookRepository;
import personal.bookerav2.service.BookService;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookeraV2ApplicationTests {

}
