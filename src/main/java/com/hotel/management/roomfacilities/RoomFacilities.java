package com.hotel.management.roomfacilities;

public class RoomFacilities {
    private int rfId;
    private int roomId;
    private String facilityName;
    private String description;
    private String status; 

    
    public int getRfId() { return rfId; }
    public void setRfId(int rfId) { this.rfId = rfId; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getFacilityName() { return facilityName; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
