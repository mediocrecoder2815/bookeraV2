package personal.bookerav2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import personal.bookerav2.entities.Book;
import personal.bookerav2.entities.Review;
import personal.bookerav2.entities.User;

import java.util.Optional;
import java.util.Set;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Set<Review> findByUser(User user);
    Set<Review> findByBook(Book book);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.bookId = :bookId")
    Double findAverageRatingByBookId(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(*) FROM Review r WHERE r.book.bookId = :bookId")
    Integer findReviewCountByBookId(@Param("bookId") Long bookId);

}
