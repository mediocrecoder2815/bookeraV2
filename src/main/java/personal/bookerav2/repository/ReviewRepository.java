package personal.bookerav2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.bookerav2.entities.Review;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
