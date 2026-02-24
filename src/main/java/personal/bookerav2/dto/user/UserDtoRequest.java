package personal.bookerav2.dto.user;

public record UserDtoRequest (
        String username,
        String hashedPassword
){
}
