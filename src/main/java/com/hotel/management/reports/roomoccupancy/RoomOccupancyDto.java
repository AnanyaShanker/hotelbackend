package com.hotel.management.reports.roomoccupancy;

import java.time.LocalDateTime;

public class RoomOccupancyDto {

    private Integer roomId;
    private String roomNumber;
    private String branchName;
    private String roomType;
    private LocalDateTime createdOn;

    private Integer totalAvailableNights;
    private Integer bookedNights;
    private Double occupancyPercent;

    private String currentStatus; // AVAILABLE / OCCUPIED / BLOCKED
    private Integer totalBookings;
    private Double totalRevenue;

    public RoomOccupancyDto() {}

    public RoomOccupancyDto(Integer roomId, String roomNumber, String branchName, String roomType,
                            LocalDateTime createdOn, Integer totalAvailableNights, Integer bookedNights,
                            Double occupancyPercent, String currentStatus, Integer totalBookings,
                            Double totalRevenue) {

        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.branchName = branchName;
        this.roomType = roomType;
        this.createdOn = createdOn;

        this.totalAvailableNights = totalAvailableNights;
        this.bookedNights = bookedNights;
        this.occupancyPercent = occupancyPercent;

        this.currentStatus = currentStatus;
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
    }

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getTotalAvailableNights() {
		return totalAvailableNights;
	}

	public void setTotalAvailableNights(Integer totalAvailableNights) {
		this.totalAvailableNights = totalAvailableNights;
	}

	public Integer getBookedNights() {
		return bookedNights;
	}

	public void setBookedNights(Integer bookedNights) {
		this.bookedNights = bookedNights;
	}

	public Double getOccupancyPercent() {
		return occupancyPercent;
	}

	public void setOccupancyPercent(Double occupancyPercent) {
		this.occupancyPercent = occupancyPercent;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Integer getTotalBookings() {
		return totalBookings;
	}

	public void setTotalBookings(Integer totalBookings) {
		this.totalBookings = totalBookings;
	}

	public Double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(Double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	@Override
	public String toString() {
		return "RoomOccupancyDto [roomId=" + roomId + ", roomNumber=" + roomNumber + ", branchName=" + branchName
				+ ", roomType=" + roomType + ", createdOn=" + createdOn + ", totalAvailableNights="
				+ totalAvailableNights + ", bookedNights=" + bookedNights + ", occupancyPercent=" + occupancyPercent
				+ ", currentStatus=" + currentStatus + ", totalBookings=" + totalBookings + ", totalRevenue="
				+ totalRevenue + "]";
	}

}