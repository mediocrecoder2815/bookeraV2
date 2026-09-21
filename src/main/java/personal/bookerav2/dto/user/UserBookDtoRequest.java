package personal.bookerav2.dto.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;




public record UserBookDtoRequest(

        @NotNull Long bookId,
        @NotNull @Max(3) @Min(1) Short statusId
) { }
