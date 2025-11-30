package com.hotel.management.facilities;

public class Facility {

    private int facilityId;
    private String name;
    private String type;
    private double price;
    private int capacity;
    private String eventStart; // ISO datetime string (or null)
    private String eventEnd;
    private String status;
    private String facilityPrimaryImage;
    private String brochureDocument;
    private String description;
    private String location;
    private String createdAt;
    private String updatedAt;

    public Facility() {
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getEventStart() {
        return eventStart;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public String getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(String eventEnd) {
        this.eventEnd = eventEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFacilityPrimaryImage() {
        return facilityPrimaryImage;
    }

    public void setFacilityPrimaryImage(String facilityPrimaryImage) {
        this.facilityPrimaryImage = facilityPrimaryImage;
    }

    public String getBrochureDocument() {
        return brochureDocument;
    }

    public void setBrochureDocument(String brochureDocument) {
        this.brochureDocument = brochureDocument;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
