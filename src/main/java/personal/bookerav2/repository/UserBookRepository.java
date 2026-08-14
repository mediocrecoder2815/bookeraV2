package personal.bookerav2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.bookerav2.entities.UserBook;
import personal.bookerav2.entities.UserBookId;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserBookRepository extends JpaRepository<UserBookId, UserBook> {
    public Set<UserBook> findByUserId_UserId(UUID userID);
}
