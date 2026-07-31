package personal.bookerav2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(initialValue = 100)
    @Column(name = "book_id")
    Integer bookId;

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
    Set<Review> reviews = new HashSet<>();

    @Column(name = "picture_url", nullable = true)
    String pictureUrl;

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", name='" + name + '\'' +
                ", isbn='" + isbn + '\'' +
                ", totalPages=" + totalPages +
                ", description='" + description + '\'' +
                ", dateOfPublish=" + dateOfPublish +
                ", authors=" + authors +
                ", categories=" + categories +
                ", reviews=" + reviews +
                ", pictureUrl='" + pictureUrl + '\'' +
                '}';
    }
}
