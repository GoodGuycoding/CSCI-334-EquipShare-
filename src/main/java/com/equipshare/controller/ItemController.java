package com.equipshare.controller;
import com.equipshare.model.Booking;
import com.equipshare.model.Item;
import com.equipshare.model.Review;
import com.equipshare.model.User;
import com.equipshare.repository.BookingRepository;
import com.equipshare.security.CustomUserDetails;
import com.equipshare.service.ItemService;
import com.equipshare.service.ReviewService;
import com.equipshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class ItemController {

    private final ItemService itemService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/tool/{id}")
    public String viewItemDetails(@PathVariable("id") String itemId,@RequestParam(required = false) String step,
                                  @RequestParam(required = false) String startDate, Model model, Principal principal) {
        Item item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("step", step); // "start" or "end"
        model.addAttribute("startDate", startDate);

        // reviews added
        List<Review> reviews = reviewService.getReviewsByItemId(item.getId());
        model.addAttribute("reviews", reviews);

        // adding current user if logged in
        if (principal != null) {
            User currentUser = userService.getUserByEmail(principal.getName()).orElseThrow();
            model.addAttribute("currentUser", currentUser);

            List<Booking> bookings = bookingRepository.findAllByUserAndItem(currentUser.getId(), item.getId());
            Booking booking = bookings.isEmpty() ? null : bookings.get(0);  // or process list
            model.addAttribute("booking", booking);
        }

        return "productPage";
    }

    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam("itemId") String itemId, @RequestParam String startDate,
                                  @RequestParam(required = false) String success,
                                  @RequestParam String endDate, Model model) {
        Item item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        if ("true".equals(success)) {
            model.addAttribute("success", true);
        }
        return "payment"; // payment.html
    }


    @GetMapping("/add-item")
    public String showAddItemForm() {
        return "add-item";
    }
    @Autowired
    private UserService userService;

    @PostMapping("/add-item")
    public String handleAddItem(@ModelAttribute Item item, Principal principal) {
        String email = principal.getName();
        User owner = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        item.setOwner(owner);
        item.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        item.setAvailable(true);

        itemService.addItem(item, owner);
        return "redirect:/ownerDashboard";
    }

    @PostMapping("/reviews")
    public String submitReview(@RequestParam("reviewerId") String reviewerId,
                               @RequestParam("bookingId") String bookingId,
                               @RequestParam("rating") int rating,
                               @RequestParam("comment") String comment) {

        System.out.println("I was hit");
        // Get reviewer and booking
        User reviewer = userService.getUserById(reviewerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        System.out.println("method's worked");

        // Create and save review
        Review review = new Review();
        review.setReviewer(reviewer);
        review.setBooking(booking);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        System.out.println("review was created");

        reviewService.saveReview(review);

        // Redirect to the product page
        String itemId = booking.getItem().getId();
        return "redirect:/tool/" + itemId;
    }




}
