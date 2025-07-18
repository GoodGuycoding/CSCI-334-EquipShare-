package com.equipshare.service;
import com.equipshare.model.Booking;
import com.equipshare.model.User;
import com.equipshare.model.Item;
import com.equipshare.repository.BookingRepository;
import com.equipshare.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;



@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;


    // Directory where product images will be stored
    private static final String UPLOAD_DIR = "uploads/products/";

    public ItemService(ItemRepository itemRepository, UserService userService, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;

        // Create upload directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public List<Item> getItemByOwner(User owner) {
        return itemRepository.findByOwner(owner);
    }

    public List<Item> getAvailableItemsByOwner(User owner) {
        List<Item> items = itemRepository.findByOwnerAndIsAvailable(owner, true);
        System.out.println("Available items for owner " + owner.getEmail() + ": " + items);
        return items;
    }

    public void addItem(Item item, User owner) {
        item.setOwner(owner);
        item.setCreatedAt(Timestamp.from(Instant.now()));
        item.setAvailable(true); // default to available

         itemRepository.save(item);
    }

    public void deleteItem(String itemId, User owner) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You can only delete your own items");
        }

        itemRepository.delete(item);
    }

    public Item updateItem(String itemId, Item updatedItem, User owner) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!existingItem.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You can only update your own items");
        }

        existingItem.setTitle(updatedItem.getTitle());
        existingItem.setDescription(updatedItem.getDescription());
        existingItem.setPricePerDay(updatedItem.getPricePerDay());
        existingItem.setLocation(updatedItem.getLocation());
        existingItem.setAvailable(updatedItem.isAvailable());

        return itemRepository.save(existingItem);
    }

    public List<Item> getRentedItemsByOwner(User owner) {
        return itemRepository.findByOwnerAndIsAvailable(owner, false);
    }

    public List<Booking> getBookingsByBorrower(User borrower) {
        return bookingRepository.findByBorrower(borrower);
    }

    public Booking createBooking(Item item, User borrower, LocalDate startDate, LocalDate endDate) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBorrower(borrower);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setStatus("PENDING");
        booking.setCreatedAt(Timestamp.from(Instant.now()));

        return bookingRepository.save(booking);
    }

    public void approveBooking(String bookingId, User owner) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getItem().getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You can only approve bookings for your own items");
        }

        booking.setStatus("APPROVED");
        bookingRepository.save(booking);

        // Change the item availiability
        Item item = booking.getItem();
        item.setAvailable(false);
        itemRepository.save(item);
    }
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    public Page<Item> getItemsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.findAll(pageable);
    }

    public Item getItemById(String id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public Page<Item> searchAndFilterPaged(String keyword, String filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (keyword != null && !keyword.isEmpty()) {
            return itemRepository.searchByKeyword(keyword.toLowerCase(), pageable);
        }

        if ("price".equals(filter)) {
            return itemRepository.findAllByOrderByPricePerDayAsc(pageable);
        } else if ("location".equals(filter)) {
            return itemRepository.findAllByOrderByLocationAsc(pageable);
        }

        return itemRepository.findAll(pageable);
    }
}