package personal.bookerav2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.bookerav2.entities.Author;

import java.util.Optional;
import java.util.Set;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Set<Author> findByBooks_BookId(Long bookId);

    @Override
    @EntityGraph(attributePaths = "books")
    Optional<Author> findById(Long id);
}
