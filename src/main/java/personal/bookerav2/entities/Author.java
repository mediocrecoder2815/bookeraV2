package personal.bookerav2.entities;


import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import personal.bookerav2.entities.utils.CountryCode;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "author")
@Getter
@Setter
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID authorId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "surname", nullable = false)
    String surname;

    @Column(name = "description")
    String description;

    @ManyToMany(mappedBy = "authors")
    Set<Book> books;

    @Column(name = "date_of_birth")
    Instant dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    CountryCode country;

    public String getFullName(){
        return country.getGetFullName();
    }
}
