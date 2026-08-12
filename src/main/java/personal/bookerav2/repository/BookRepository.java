package personal.bookerav2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.bookerav2.entities.Book;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Set<Book> findByAuthors_AuthorId(Long authorId);

    @Override
    @EntityGraph(attributePaths = {"authors", "reviews", "reviews.user"})
    Optional<Book> findById(Long id);
}

