package personal.bookerav2.dto.reviews;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReviewDtoRequest(
        @NotBlank String content,
        @NotNull String username,
        @NotNull Short rating
){
    public ReviewDtoRequest{
        if(rating > 5 || rating < 0){
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
    }
}
