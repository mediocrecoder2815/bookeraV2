package personal.bookerav2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.bookerav2.entities.Review;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
