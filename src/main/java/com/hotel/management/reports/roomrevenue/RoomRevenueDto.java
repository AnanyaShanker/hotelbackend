package com.hotel.management.reports.roomrevenue;

import java.math.BigDecimal;

public class RoomRevenueDto {

    private String roomType;
    private BigDecimal revenue;
    private Integer totalBookings;
    private Integer bookedNights;
    private BigDecimal avgRevenuePerBooking;

    public RoomRevenueDto() {}

    public RoomRevenueDto(String roomType, BigDecimal revenue, Integer totalBookings, Integer bookedNights, BigDecimal avgRevenuePerBooking) {
        this.roomType = roomType;
        this.revenue = revenue;
        this.totalBookings = totalBookings;
        this.bookedNights = bookedNights;
        this.avgRevenuePerBooking = avgRevenuePerBooking;
    }

    public String getRoomType() {
        return roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }
    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public Integer getTotalBookings() {
        return totalBookings;
    }
    public void setTotalBookings(Integer totalBookings) {
        this.totalBookings = totalBookings;
    }

    public Integer getBookedNights() {
        return bookedNights;
    }
    public void setBookedNights(Integer bookedNights) {
        this.bookedNights = bookedNights;
    }

    public BigDecimal getAvgRevenuePerBooking() {
        return avgRevenuePerBooking;
    }
    public void setAvgRevenuePerBooking(BigDecimal avgRevenuePerBooking) {
        this.avgRevenuePerBooking = avgRevenuePerBooking;
    }
}
