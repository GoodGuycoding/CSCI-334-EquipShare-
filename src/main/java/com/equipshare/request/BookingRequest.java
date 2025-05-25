package com.equipshare.request;

import java.time.LocalDate;

public class BookingRequest {

    private String itemId;
    private LocalDate startDate;
    private LocalDate endDate;

    // Getters and Setters
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
