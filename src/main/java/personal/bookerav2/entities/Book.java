package personal.bookerav2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    Long bookId;

    @Column(name = "name", nullable = false, length = 50)
    String name;

    @Column(name = "ISBN", nullable = false, length = 14)
    String isbn;

    @Column(name = "total_pages", nullable = false)
    Short totalPages;

    @Column(name = "description")
    String description;

    @Column(name = "date_of_publish")
    LocalDate dateOfPublish;

    @ManyToMany
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @Builder.Default
    Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Builder.Default
    Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "book",
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    @Builder.Default
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
