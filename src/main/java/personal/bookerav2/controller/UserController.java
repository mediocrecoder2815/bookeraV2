package personal.bookerav2.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.bookerav2.dto.user.UserBookDtoRequest;
import personal.bookerav2.dto.user.UserBooksDto;
import personal.bookerav2.dto.user.UserDtoResponse;
import personal.bookerav2.dto.user.UserShelfDto;
import personal.bookerav2.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")

@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDtoResponse> getMe(Principal principal){
        return ResponseEntity.ok(userService.getMe(principal));
    }

    @PostMapping("/shelf")
    public ResponseEntity<UserDtoResponse> addBook(Principal principal, @RequestBody UserBookDtoRequest userBook){
        return ResponseEntity.ok(userService.addBook(principal, userBook));
    }

    @PutMapping("/shelf")
    public ResponseEntity<UserDtoResponse> updateBookStatus(Principal principal, @RequestBody UserBookDtoRequest bookUpdate){
        return ResponseEntity.ok(userService.updateStatus(principal, bookUpdate));
    }

    @GetMapping("/shelf")
    public ResponseEntity<UserShelfDto> getShelf(Principal principal){
        return ResponseEntity.ok(userService.getShelf(principal));
    }
}
