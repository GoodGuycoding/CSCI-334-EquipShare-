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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;
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


        if (request == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid booking data.");
            return "redirect:/tools";
        }


        User borrower = userDetails.getUser();

        Optional<Item> item = itemRepository.findById(request.getItemId());
        if (item.isEmpty()) {
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


        notificationService.sendNotification(
                borrower,
                "Booking confirmed for: " + actualItem.getTitle(),
                NotificationType.BOOKING
        );

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

    @GetMapping("/borrower-dashboard")
    public String showBorrowerDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        List<Object[]> bookingsPerDay = bookingRepository.findTotalBookingsGroupedByDay();
        model.addAttribute("bookingsPerDay", bookingsPerDay);

        return "borrowerDashboard";
    }

}

