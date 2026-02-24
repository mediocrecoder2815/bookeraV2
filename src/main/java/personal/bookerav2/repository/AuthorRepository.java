package personal.bookerav2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.bookerav2.entities.Author;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
}
