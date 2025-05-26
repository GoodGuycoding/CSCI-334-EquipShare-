package com.equipshare.controller;

import com.equipshare.model.Rating;
import com.equipshare.model.User;
import com.equipshare.repository.RatingRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.equipshare.security.CustomUserDetails;
import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    private final RatingRepository ratingRepository;

    public RatingController(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @GetMapping("/my-ratings")
    public ResponseEntity<List<Rating>> getMyRatings(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        List<Rating> ratings = ratingRepository.findByBorrower(currentUser);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/received-ratings")
    public ResponseEntity<List<Rating>> getReceivedRatings(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        List<Rating> ratings = ratingRepository.findByItemOwner(currentUser);
        return ResponseEntity.ok(ratings);
    }

    @PostMapping
    public ResponseEntity<Rating> createRating(
            @RequestParam String bookingId,
            @RequestParam int score,
            @RequestParam(required = false) String comment,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Rating rating = new Rating();
        rating.setId(java.util.UUID.randomUUID().toString());
        rating.setScore(score);
        rating.setComment(comment);
        rating.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        Rating savedRating = ratingRepository.save(rating);
        return ResponseEntity.ok(savedRating);
    }
}