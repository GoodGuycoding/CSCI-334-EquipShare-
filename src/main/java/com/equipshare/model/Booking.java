package com.equipshare.model;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "borrower_id", nullable = false)
    private User borrower;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status")
    private String status; // "PENDING", "APPROVED", "REJECTED", "COMPLETED"

    @Column(name = "created_at")
    private Timestamp createdAt;

    public Booking() {

    }

    // Getters
    public String getId() {
        return id;
    }
    public Item getItem() {
        return item;
    }
    public User getBorrower() {
        return borrower;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }
    public void setItem(Item item) {
        this.item = item;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}