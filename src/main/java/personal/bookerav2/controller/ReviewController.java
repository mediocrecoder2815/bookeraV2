package personal.bookerav2.controller;


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
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{bookId}")
    public ResponseEntity<ReviewDtoResponse> createReview(@PathVariable Long bookId, @RequestBody @Valid ReviewDtoRequest reviewDto, Principal principal){
        return ResponseEntity.ok(reviewService.createReview(reviewDto, bookId, principal));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDtoResponse> updateReview(@PathVariable Long id, @RequestBody @Valid ReviewDtoRequest review){
        return ResponseEntity.ok(reviewService.updateReview(review, id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
}
