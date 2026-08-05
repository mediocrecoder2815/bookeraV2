package personal.bookerav2.entities;


import jakarta.persistence.*;
import lombok.*;
import personal.bookerav2.entities.enums.CountryCode;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "author_id")
    @SequenceGenerator(initialValue = 100)
    Integer authorId;

    @Column(name = "name", nullable = false, length = 25)
    String name;

    @Column(name = "surname", nullable = false, length = 25)
    String surname;

    @Column(name = "description")
    String description;

    @ManyToMany(mappedBy = "authors")
    Set<Book> books = new HashSet<>();

    @Column(name = "date_of_birth")
    Instant dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    CountryCode country;


    @Column(name = "photo_url", nullable = true)
    String pictureUrl;

    public String getFullName(){
        return country.getGetFullName();
    }
}
