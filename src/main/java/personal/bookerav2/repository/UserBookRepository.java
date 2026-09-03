package personal.bookerav2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.bookerav2.entities.UserBook;
import personal.bookerav2.entities.UserBookId;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Repository
public interface UserBookRepository extends JpaRepository<UserBook, UserBookId> {
    Set<UserBook> findByUserId_UserId(UUID userID);
}
