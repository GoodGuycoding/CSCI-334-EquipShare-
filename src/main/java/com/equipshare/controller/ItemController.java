package com.equipshare.controller;
import com.equipshare.model.Item;
import com.equipshare.security.CustomUserDetails;
import com.equipshare.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/tool/{id}")
    public String viewItemDetails(@PathVariable("id") String itemId,@RequestParam(required = false) String step,
                                  @RequestParam(required = false) String startDate, Model model) {
        Item item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("step", step); // "start" or "end"
        model.addAttribute("startDate", startDate);
        return "productPage"; // This matches product.html
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
            model.addAttribute("success", true); // ✅ pass flag to show modal
        }
        return "payment"; // payment.html
    }


    @PostMapping("/add")
    public String addItem(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam double pricePerDay,
            @RequestParam String location,
            @RequestParam("imageFile") MultipartFile imageFile,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Item item = new Item();
        item.setTitle(title);
        item.setDescription(description);
        item.setPricePerDay(pricePerDay);
        item.setLocation(location);
        item.setAvailable(true);

        try {
            itemService.addItem(item, imageFile, userDetails.getUser());
            return "redirect:/owner-dashboard?success";
        } catch (Exception e) {
            return "redirect:/owner-dashboard?error";
        }
    }


}
