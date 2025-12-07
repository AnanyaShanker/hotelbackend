package com.hotel.management.feedback;

/**
 * Simple view for admin dashboard.
 * One row per feedback.
 */
public class FeedbackDisplayDto {

    private Integer feedbackId;
    private String customerName; // from users.name
    private String itemName;     // "Room 101" OR facility name
    private Integer rating;
    private String comments;

    public FeedbackDisplayDto() {}

    public FeedbackDisplayDto(Integer feedbackId, String customerName,
                              String itemName, Integer rating, String comments) {
        this.feedbackId = feedbackId;
        this.customerName = customerName;
        this.itemName = itemName;
        this.rating = rating;
        this.comments = comments;
    }

    public Integer getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Integer feedbackId) { this.feedbackId = feedbackId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}