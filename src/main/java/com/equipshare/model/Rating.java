package com.equipshare.model;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.sql.Timestamp;
import jakarta.persistence.ManyToOne;

@Entity
public class Rating {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private User borrower;

    private int score;
    private String comment;
    private Timestamp createdAt;

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

    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }


    //Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }



    public void setScore(int score) {
        this.score = score;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}