package com.equipshare.service;
import com.equipshare.model.User;
import com.equipshare.model.Item;
import com.equipshare.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    // Directory where product images will be stored
    private static final String UPLOAD_DIR = "uploads/products/";

    public ItemService(ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;

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
        return itemRepository.findByOwnerAndIsAvailable(owner, true);
    }

    public Item addItem(Item item, MultipartFile imageFile, User owner) throws IOException {
        // Handle image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(imageFile.getInputStream(), filePath);
            item.setItemPhotoUrl("/" + UPLOAD_DIR + fileName);
        }

        item.setOwner(owner);
        item.setCreatedAt(Timestamp.from(Instant.now()));
        return itemRepository.save(item);
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



}