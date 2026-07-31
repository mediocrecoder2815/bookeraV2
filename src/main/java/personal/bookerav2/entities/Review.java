package personal.bookerav2.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "review_id")
    @SequenceGenerator(initialValue = 100)
    Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "rating", nullable = false)
    Integer rating;

    @Column(name = "content")
    String content;

}
