package com.equipshare.service;

import com.equipshare.model.Review;
import com.equipshare.model.Booking;
import com.equipshare.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviewsByItemId(String itemId) {
        return reviewRepository.findByItemId(itemId);
    }

    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    public boolean hasUserAlreadyReviewedBooking(Booking booking) {
        return reviewRepository.findByBooking(booking).isPresent();
    }

    public Optional<Review> getReviewByBooking(Booking booking) {
        return reviewRepository.findByBooking(booking);
    }
}
