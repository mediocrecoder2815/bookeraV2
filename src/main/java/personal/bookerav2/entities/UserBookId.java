package personal.bookerav2.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
public class UserBookId implements Serializable {
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "book_id")
    private Long bookId;

    public UserBookId(){}
    public UserBookId(UUID userId, long bookId){
        this.userId = userId;
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        }
        if(!(o instanceof UserBookId that))
            return false;
        return ((UserBookId) o).bookId == this.bookId
                && ((UserBookId) o).userId.equals(this.userId);
    }
    @Override
    public int hashCode(){
        return Objects.hash(this.userId, this.bookId);
    }

}
