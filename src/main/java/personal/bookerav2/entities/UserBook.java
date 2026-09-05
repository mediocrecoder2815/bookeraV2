package personal.bookerav2.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private User userId;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book bookId;

    @Column(name = "status_id")
    private short bookStatus;
}
