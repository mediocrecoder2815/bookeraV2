package personal.bookerav2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.bookerav2.entities.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
