package personal.bookerav2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.bookerav2.entities.Author;

import java.util.Set;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Set<Author> findByBooks_BookId(Integer bookId);
}
