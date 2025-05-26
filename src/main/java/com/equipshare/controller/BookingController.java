package com.equipshare.controller;

import com.equipshare.model.NotificationType;
import com.equipshare.model.User;
import com.equipshare.model.Booking;
import com.equipshare.model.Item;
import com.equipshare.repository.BookingRepository;
import com.equipshare.repository.ItemRepository;
import com.equipshare.security.CustomUserDetails;
import com.equipshare.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class BookingController {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    @Autowired
    private NotificationService notificationService;

    public BookingController(BookingRepository bookingRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
    }

    @PostMapping("/confirm-booking")
    public String confirmBooking(
            @ModelAttribute BookingRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        System.out.println("⚙️ Received confirm-booking request");

        if (request == null) {
            System.out.println("❌ BookingRequest is null");
            redirectAttributes.addFlashAttribute("error", "Invalid booking data.");
            return "redirect:/tools";
        }

        System.out.println("📦 Item ID: " + request.getItemId());
        System.out.println("📅 Start Date: " + request.getStartDate());
        System.out.println("📅 End Date: " + request.getEndDate());

        User borrower = userDetails.getUser();
        System.out.println("👤 Borrower ID: " + borrower.getId());

        Optional<Item> item = itemRepository.findById(request.getItemId());
        if (item.isEmpty()) {
            System.out.println("❌ Item not found for ID: " + request.getItemId());
            redirectAttributes.addFlashAttribute("error", "Item not found");
            return "redirect:/tools";
        }

        Item actualItem = item.get();

        Booking booking = new Booking();
        booking.setItem(item.get());
        booking.setBorrower(borrower);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setStatus("CONFIRMED");
        booking.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        bookingRepository.save(booking);
        System.out.println("✅ Booking saved successfully with ID: " + booking.getId());

        System.out.println("Notification was hit");
        notificationService.sendNotification(
                borrower,
                "Booking confirmed for: " + actualItem.getTitle(),
                NotificationType.BOOKING
        );

        System.out.println("Notification was hit");
        notificationService.sendNotification(
                actualItem.getOwner(),
                "Your item '" + actualItem.getTitle() + "' has been booked.",
                NotificationType.BOOKING
        );

        return "redirect:/payment?success=true&itemId=" + request.getItemId()
                + "&startDate=" + request.getStartDate()
                + "&endDate=" + request.getEndDate();
    }

    public static class BookingRequest {
        private String itemId;
        private LocalDate startDate;
        private LocalDate endDate;

        public String getItemId() {
            return itemId;
        }
        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public LocalDate getStartDate() {
            return startDate;
        }
        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }
        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }
    }
}




//package com.equipshare.controller;
//import com.equipshare.model.User;
//import com.equipshare.request.BookingRequest;
//import com.equipshare.service.UserService;
//import com.equipshare.service.ItemService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import java.util.List;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import com.equipshare.security.CustomUserDetails;
//import com.equipshare.model.Booking;
//import com.equipshare.model.Item;
//import com.equipshare.repository.BookingRepository;
//import com.equipshare.repository.ItemRepository;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.time.LocalDate;
//import java.util.Optional;
//import com.equipshare.request.BookingRequest;
//
//@RestController
//@RequestMapping("/api/bookings")
//public class BookingController {
//
//    private final BookingRepository bookingRepository;
//    private final ItemRepository itemRepository;
//
//    public BookingController(BookingRepository bookingRepository, ItemRepository itemRepository) {
//        this.bookingRepository = bookingRepository;
//        this.itemRepository = itemRepository;
//    }
//
//    @PostMapping("/confirm-booking")
//    public String confirmBooking(
//            @ModelAttribute BookingRequest request,
//            @AuthenticationPrincipal CustomUserDetails userDetails,
//            RedirectAttributes redirectAttributes) {
//
//        System.out.println("⚙️ Received confirm-booking request");
//
//        if (request == null) {
//            System.out.println("❌ BookingRequest is null");
//            redirectAttributes.addFlashAttribute("error", "Invalid booking data.");
//            return "redirect:/tools";
//        }
//
//        System.out.println("📦 Item ID: " + request.getItemId());
//        System.out.println("📅 Start Date: " + request.getStartDate());
//        System.out.println("📅 End Date: " + request.getEndDate());
//
//        User borrower = userDetails.getUser();
//        System.out.println("👤 Borrower ID: " + borrower.getId());
//
//        Optional<Item> item = itemRepository.findById(request.getItemId());
//        if (item.isEmpty()) {
//            System.out.println("❌ Item not found for ID: " + request.getItemId());
//            redirectAttributes.addFlashAttribute("error", "Item not found");
//            return "redirect:/tools";
//        }
//
//        Booking booking = new Booking();
//        booking.setItem(item.get());
//        booking.setBorrower(borrower);
//        booking.setStartDate(request.getStartDate());
//        booking.setEndDate(request.getEndDate());
//        booking.setStatus("CONFIRMED");
//        booking.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
//
//        bookingRepository.save(booking);
//        System.out.println("✅ Booking saved successfully with ID: " + booking.getId());
//
//        return "redirect:/payment?success=true";
//    }
//
//    @PostMapping("/{bookingId}/approve")
//    public Booking approveBooking(
//            @PathVariable String bookingId,
//            @AuthenticationPrincipal CustomUserDetails userDetails) {
//
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        if (!booking.getItem().getOwner().getId().equals(userDetails.getUser().getId())) {
//            throw new RuntimeException("Not authorized to approve this booking");
//        }
//
//        booking.setStatus("APPROVED");
//        booking.getItem().setAvailable(false);
//        itemRepository.save(booking.getItem());
//
//        return bookingRepository.save(booking);
//    }
//
//    @PostMapping("/{bookingId}/return")
//    public Booking returnBooking(
//            @PathVariable String bookingId,
//            @AuthenticationPrincipal CustomUserDetails userDetails) {
//
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        if (!booking.getBorrower().getId().equals(userDetails.getUser().getId())) {
//            throw new RuntimeException("Not authorized to return this booking");
//        }
//
//        booking.setStatus("COMPLETED");
//        booking.getItem().setAvailable(true);
//        itemRepository.save(booking.getItem());
//
//        return bookingRepository.save(booking);
//    }
//
//    @PostMapping("/{bookingId}/reject")
//    public Booking rejectBooking(
//            @PathVariable String bookingId,
//            @AuthenticationPrincipal CustomUserDetails userDetails) {
//
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        if (!booking.getItem().getOwner().getId().equals(userDetails.getUser().getId())) {
//            throw new RuntimeException("Not authorized to reject this booking");
//        }
//
//        booking.setStatus("REJECTED");
//        return bookingRepository.save(booking);
//    }
//
//    @GetMapping("/owner")
//    public List<Booking> getOwnerBookings(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        return bookingRepository.findByItemOwner(userDetails.getUser());
//    }
//
//    @PostMapping("/{bookingId}/rate")
//    public Booking rateBooking(
//            @PathVariable String bookingId,
//            @RequestBody RatingRequest request,
//            @AuthenticationPrincipal CustomUserDetails userDetails) {
//
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        if (!booking.getBorrower().getId().equals(userDetails.getUser().getId())) {
//            throw new RuntimeException("Not authorized to rate this booking");
//        }
//
//        if (!"COMPLETED".equals(booking.getStatus())) {
//            throw new RuntimeException("Can only rate completed bookings");
//        }
//
//    // Need code to save ratings
//
//        return booking;
//    }
//
//    public static class BookingRequest {
//        private String itemId;
//        private LocalDate startDate;
//        private LocalDate endDate;
//
//        // Getters
//        public String getItemId() {
//            return itemId;
//        }
//        public LocalDate getStartDate() {
//            return startDate;
//        }
//        public LocalDate getEndDate() {
//            return endDate;
//        }
//
//        // Setters
//        public void setItemId(String itemId) {
//            this.itemId = itemId;
//        }
//
//        public void setStartDate(LocalDate startDate) {
//            this.startDate = startDate;
//        }
//        public void setEndDate(LocalDate endDate) {
//            this.endDate = endDate;
//        }
//    }
//
//    public static class RatingRequest {
//        private int rating;
//
//        // Getters and setters
//        public int getRating() {
//            return rating;
//        }
//        public void setRating(int rating) {
//            this.rating = rating;
//        }
//    }
//}