package personal.bookerav2.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "'user'")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    UUID userId;

    @Column(name = "username", nullable = false)
    String username;

    @Column(name = "name")
    String name;

    @Column(name = "surname")
    String surname;

    @Column(name = "hashed_password")
    String hashedPassword;

    @ManyToMany
    @JoinTable(
            name = "book_user",
            joinColumns = @JoinColumn(name ="user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    Set<Book> books = new HashSet<>();

    @Column(name = "avatar_url")
    String avatarUrl;
}
