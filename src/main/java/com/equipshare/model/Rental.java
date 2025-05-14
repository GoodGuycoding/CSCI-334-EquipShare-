package com.equipshare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Rental {
    private String ItemID;
    private String borrowerId;
    private String startDate;
    private String endDate;
    private float totalCost;
    private enum status;
    private Boolean depositHeld;
    private enum paymentStatus;
    @Id
    private Long rentalId;

    public void setId(Long id) {
        this.rentalId = rentalId;
    }

    public Long getrentalId() {
        return rentalId;
    }



    private float calculateTotalCost() {
    return totalCost;
    }
    private boolean markAsReturned() {
    return true;
    }

}