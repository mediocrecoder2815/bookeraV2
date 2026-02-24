package personal.bookerav2.dto.reviews;

import java.util.UUID;

public record ReviewDtoRequest(
        String content,
        UUID bookId,
        UUID userId,
        Integer rating
){
    public ReviewDtoRequest{
        if(rating > 5 || rating < 0){
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
    }
}
