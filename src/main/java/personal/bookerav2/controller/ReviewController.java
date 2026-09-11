package personal.bookerav2.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.bookerav2.dto.reviews.ReviewDtoRequest;
import personal.bookerav2.dto.reviews.ReviewDtoResponse;
import personal.bookerav2.service.ReviewService;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Endpoints for managing reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "Create a review for a book",
            description = "Requires authentication. The authenticated user becomes the review author.")
    @Parameter(name = "bookId", description = "ID of the book being reviewed")
    @ApiResponse(responseCode = "200", description = "Review created",
            content = @Content(schema = @Schema(implementation = ReviewDtoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Book not found")
    @PostMapping("/{bookId}")
    public ResponseEntity<ReviewDtoResponse> createReview(@PathVariable Long bookId, @RequestBody @Valid ReviewDtoRequest reviewDto, Principal principal){
        return ResponseEntity.ok(reviewService.createReview(reviewDto, bookId, principal));
    }

    @Operation(summary = "Update a review by ID",
            description = "Requires authentication. Only the review author can update it.")
    @Parameter(name = "id", description = "Review ID")
    @ApiResponse(responseCode = "200", description = "Review updated",
            content = @Content(schema = @Schema(implementation = ReviewDtoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Review not found")
    @ApiResponse(responseCode = "403", description = "Not the review author")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDtoResponse> updateReview(@PathVariable Long id, @RequestBody @Valid ReviewDtoRequest review, Principal principal){
        return ResponseEntity.ok(reviewService.updateReview(review, id, principal));
    }

    @Operation(summary = "Delete a review by ID",
            description = "Requires authentication. Only the review author can delete it.")
    @Parameter(name = "id", description = "Review ID")
    @ApiResponse(responseCode = "200", description = "Review deleted")
    @ApiResponse(responseCode = "404", description = "Review not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, Principal principal){
        reviewService.deleteReview(id, principal);
        return ResponseEntity.ok().build();
    }
}
