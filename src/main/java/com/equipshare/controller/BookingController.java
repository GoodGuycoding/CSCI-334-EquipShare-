package com.equipshare.controller;
import com.equipshare.model.User;
import com.equipshare.service.UserService;
import com.equipshare.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.equipshare.security.CustomUserDetails;
import com.equipshare.model.Booking;
import com.equipshare.model.Item;
import com.equipshare.repository.BookingRepository;
import com.equipshare.repository.ItemRepository;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    public BookingController(BookingRepository bookingRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
    }

    @PostMapping
    public Booking createBooking(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        User borrower = userDetails.getUser();
        Optional<Item> item = itemRepository.findById(request.getItemId());

        if (item.isEmpty()) {
            throw new RuntimeException("Item not found");
        }

        Booking booking = new Booking();
        booking.setItem(item.get());
        booking.setBorrower(borrower);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setStatus("PENDING");
        booking.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        return bookingRepository.save(booking);
    }

    @PostMapping("/{bookingId}/approve")
    public Booking approveBooking(
            @PathVariable String bookingId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getItem().getOwner().getId().equals(userDetails.getUser().getId())) {
            throw new RuntimeException("Not authorized to approve this booking");
        }

        booking.setStatus("APPROVED");
        booking.getItem().setAvailable(false);
        itemRepository.save(booking.getItem());

        return bookingRepository.save(booking);
    }

    @PostMapping("/{bookingId}/return")
    public Booking returnBooking(
            @PathVariable String bookingId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getBorrower().getId().equals(userDetails.getUser().getId())) {
            throw new RuntimeException("Not authorized to return this booking");
        }

        booking.setStatus("COMPLETED");
        booking.getItem().setAvailable(true);
        itemRepository.save(booking.getItem());

        return bookingRepository.save(booking);
    }

    @PostMapping("/{bookingId}/rate")
    public Booking rateBooking(
            @PathVariable String bookingId,
            @RequestBody RatingRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getBorrower().getId().equals(userDetails.getUser().getId())) {
            throw new RuntimeException("Not authorized to rate this booking");
        }

        if (!"COMPLETED".equals(booking.getStatus())) {
            throw new RuntimeException("Can only rate completed bookings");
        }

    // Need code to save ratings

        return booking;
    }

    public static class BookingRequest {
        private String itemId;
        private LocalDate startDate;
        private LocalDate endDate;

        // Getters
        public String getItemId() {
            return itemId;
        }
        public LocalDate getStartDate() {
            return startDate;
        }
        public LocalDate getEndDate() {
            return endDate;
        }

        // Setters
        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }
        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }
    }

    public static class RatingRequest {
        private int rating;

        // Getters and setters
        public int getRating() {
            return rating;
        }
        public void setRating(int rating) {
            this.rating = rating;
        }
    }
}