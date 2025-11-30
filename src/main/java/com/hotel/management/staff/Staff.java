package com.hotel.management.staff;

public class Staff {
    private Integer staffId;
    private Integer userId;
    private Integer hotelId;
    private String status; 

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getHotelId() { return hotelId; }
    public void setHotelId(Integer hotelId) { this.hotelId = hotelId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

