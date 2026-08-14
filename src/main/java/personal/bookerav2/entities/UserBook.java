package personal.bookerav2.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import personal.bookerav2.entities.enums.BookStatus;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "user_books")
public class UserBook {
    @EmbeddedId
    private UserBookId userBookId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UUID userId;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Long bookId;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_status")
    private BookStatus bookStatus;
}
