package personal.bookerav2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.bookerav2.entities.Book;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
}

