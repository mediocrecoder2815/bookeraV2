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
    private UUID userID;
    @Column(name = "book_id")
    private Long bookId;

    public UserBookId(){}
    public UserBookId(UUID userId, long bookId){
        this.userID = userId;
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
                && ((UserBookId) o).userID.equals(this.userID);
    }
    @Override
    public int hashCode(){
        return Objects.hash(this.userID, this.bookId);
    }

}
