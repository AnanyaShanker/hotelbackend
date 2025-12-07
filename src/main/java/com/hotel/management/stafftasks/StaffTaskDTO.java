package com.hotel.management.stafftasks;

import java.sql.Timestamp;

public class StaffTaskDTO {
    private Integer taskId;
    private Integer staffId;
    private Integer roomId;
    private String taskType;
    private String status;
    private String remarks;
    private Timestamp assignedAt;
    private Timestamp completedAt;

    // Enriched fields for frontend convenience
    private String staffName;
    private Integer staffUserId;
    private String roomNumber;

    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Timestamp getAssignedAt() { return assignedAt; }
    public void setAssignedAt(Timestamp assignedAt) { this.assignedAt = assignedAt; }

    public Timestamp getCompletedAt() { return completedAt; }
    public void setCompletedAt(Timestamp completedAt) { this.completedAt = completedAt; }

    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }

    public Integer getStaffUserId() { return staffUserId; }
    public void setStaffUserId(Integer staffUserId) { this.staffUserId = staffUserId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
}
