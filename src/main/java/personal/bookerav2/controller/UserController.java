package personal.bookerav2.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import personal.bookerav2.dto.user.UserBookDtoRequest;
import personal.bookerav2.dto.user.UserBooksDto;
import personal.bookerav2.dto.user.UserDtoResponse;
import personal.bookerav2.dto.user.UserShelfDto;
import personal.bookerav2.exceptions.InvalidCredentialsException;
import personal.bookerav2.service.UserService;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/users")

@AllArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing user profiles and shelves")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get current user profile",
            description = "Returns the profile of the authenticated user.")
    @ApiResponse(responseCode = "200", description = "User profile",
            content = @Content(schema = @Schema(implementation = UserDtoResponse.class)))
    @GetMapping("/me")
    public ResponseEntity<UserDtoResponse> getMe(Principal principal){
        return ResponseEntity.ok(userService.getMe(requirePrincipal(principal)));
    }

    @Operation(summary = "Add a book to the user's shelf",
            description = "Requires authentication. Adds a book to the authenticated user's reading shelf.")
    @ApiResponse(responseCode = "200", description = "Book added, updated user returned",
            content = @Content(schema = @Schema(implementation = UserDtoResponse.class)))
    @PostMapping("/shelf")
    public ResponseEntity<UserDtoResponse> addBook(Principal principal, @Valid @RequestBody UserBookDtoRequest userBook){
        return ResponseEntity.ok(userService.addBook(requirePrincipal(principal), userBook));
    }

    @Operation(summary = "Update a book's status on the shelf",
            description = "Requires authentication. Updates the reading status of a book in the authenticated user's shelf.")
    @ApiResponse(responseCode = "200", description = "Status updated, updated user returned",
            content = @Content(schema = @Schema(implementation = UserDtoResponse.class)))
    @PutMapping("/shelf")
    public ResponseEntity<UserDtoResponse> updateBookStatus(Principal principal, @Valid @RequestBody UserBookDtoRequest bookUpdate){
        return ResponseEntity.ok(userService.updateStatus(requirePrincipal(principal), bookUpdate));
    }

    @Operation(summary = "Get the user's shelf",
            description = "Requires authentication. Returns the authenticated user's reading shelf.")
    @ApiResponse(responseCode = "200", description = "User shelf",
            content = @Content(schema = @Schema(implementation = UserShelfDto.class)))
    @GetMapping("/shelf")
    public ResponseEntity<UserShelfDto> getShelf(Principal principal){
        return ResponseEntity.ok(userService.getShelf(requirePrincipal(principal)));
    }


    @Operation(summary = "Avatar upload")
    @ApiResponse(responseCode = "200", description = "Status updated, updated user returned",
            content = @Content(schema = @Schema(implementation = UserDtoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Something want wrong, file was not found")
    @PostMapping("/upload")
    public ResponseEntity<UserDtoResponse> uploadAvatar(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        return ResponseEntity.ok(userService.uploadAvatar(requirePrincipal(principal), file));
    }


    private Principal requirePrincipal(Principal principal) {
        if (principal == null) {
            throw new InvalidCredentialsException("Authentication required");
        }
        return principal;
    }
}
