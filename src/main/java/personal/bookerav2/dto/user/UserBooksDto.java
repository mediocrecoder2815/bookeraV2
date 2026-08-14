package personal.bookerav2.dto.user;

import personal.bookerav2.entities.enums.BookStatus;

import java.util.List;

public record UserBooksDto(
        String bookName,
        String pictureUrl,
        List<String> authorName,
        BookStatus bookStatus
){
}
