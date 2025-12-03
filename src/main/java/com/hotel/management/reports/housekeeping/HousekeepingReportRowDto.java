package com.hotel.management.reports.housekeeping;

import java.time.LocalDateTime;

public class HousekeepingReportRowDto {

    private Integer taskId;
    private String staffName;
    private String roomNumber;
    private String branchName;
    private String taskType;
    private String status;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
    private Long timeTakenMinutes;

    public HousekeepingReportRowDto(Integer taskId, String staffName, String roomNumber,
                                    String branchName, String taskType, String status,
                                    LocalDateTime assignedAt, LocalDateTime completedAt,
                                    Long timeTakenMinutes) {
        this.taskId = taskId;
        this.staffName = staffName;
        this.roomNumber = roomNumber;
        this.branchName = branchName;
        this.taskType = taskType;
        this.status = status;
        this.assignedAt = assignedAt;
        this.completedAt = completedAt;
        this.timeTakenMinutes = timeTakenMinutes;
    }

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
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

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getAssignedAt() {
		return assignedAt;
	}

	public void setAssignedAt(LocalDateTime assignedAt) {
		this.assignedAt = assignedAt;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}

	public Long getTimeTakenMinutes() {
		return timeTakenMinutes;
	}

	public void setTimeTakenMinutes(Long timeTakenMinutes) {
		this.timeTakenMinutes = timeTakenMinutes;
	}

	@Override
	public String toString() {
		return "HousekeepingReportRowDto [taskId=" + taskId + ", staffName=" + staffName + ", roomNumber=" + roomNumber
				+ ", branchName=" + branchName + ", taskType=" + taskType + ", status=" + status + ", assignedAt="
				+ assignedAt + ", completedAt=" + completedAt + ", timeTakenMinutes=" + timeTakenMinutes + "]";
	}

   
}