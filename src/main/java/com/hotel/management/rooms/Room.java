package com.hotel.management.rooms;

import java.sql.Timestamp;

public class Room {
    private int roomId;
    private Integer branchId;
    private int typeId;
    private String roomNumber;
    private double pricePerNight;
    private int capacity;
    private String status;   
    private String roomPrimaryImage;
    private String description;
    private Integer floorNumber;
   private Timestamp lastCleaned;
    private Timestamp createdAt;
    private Timestamp updatedAt;

   
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public Integer getBranchId() { return branchId; }
    public void setBranchId(Integer branchId) { this.branchId = branchId; }

    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRoomPrimaryImage() { return roomPrimaryImage; }
    public void setRoomPrimaryImage(String roomPrimaryImage) { this.roomPrimaryImage = roomPrimaryImage; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getFloorNumber() { return floorNumber; }
    public void setFloorNumber(Integer floorNumber) { this.floorNumber = floorNumber; }

    public Timestamp getLastCleaned() { return lastCleaned; }
   public void setLastCleaned(Timestamp lastCleaned) { this.lastCleaned = lastCleaned; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
