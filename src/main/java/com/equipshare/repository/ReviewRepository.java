package com.equipshare.repository;

import com.equipshare.model.Review;
import com.equipshare.model.Booking;
import com.equipshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    @Query("SELECT r FROM Review r WHERE r.booking.item.id = :itemId")
    List<Review> findByItemId(String itemId);

    Optional<Review> findByBooking(Booking booking);

    List<Review> findByReviewer(User reviewer);
}
