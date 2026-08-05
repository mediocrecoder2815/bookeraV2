package personal.bookerav2.dto.authors;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public record AuthorBookDto (
        @NotBlank Integer bookId,
        @NotBlank @Size(max = 50) String name,
        @NotBlank String pictureUrl,
        @NotBlank @Size(max = 13) String isbn
){
}
