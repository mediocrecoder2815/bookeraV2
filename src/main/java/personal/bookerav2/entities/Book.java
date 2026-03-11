package personal.bookerav2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "book_id")
    UUID bookId;

    @Column(name = "name", nullable = false, length = 50)
    String name;

    @Column(name = "ISBN", nullable = false, length = 20)
    String isbn;

    @Column(name = "total_pages")
    Integer totalPages;

    @Column(name = "description")
    String description;

    @Column(name = "date_of_publish")
    Instant dateOfPublish;

    @ManyToMany
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> categories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    Set<Review> reviews;

    @Column(name = "picture_url", nullable = true)
    String pictureUrl;

}
