package com.hotel.management.stafftasks;

import java.sql.Timestamp;

public class StaffTask {
    private int taskId;
    private int staffId;
    private Integer roomId;
    private String taskType;
    private String status; 
    private Timestamp assignedAt;
    private Timestamp completedAt;
    private String remarks;

    // Transient/enriched fields (not persisted by save, but filled by queries/joins)
    private String staffName;
    private Integer staffUserId;
    private String roomNumber;

    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getAssignedAt() { return assignedAt; }
    public void setAssignedAt(Timestamp assignedAt) { this.assignedAt = assignedAt; }

    public Timestamp getCompletedAt() { return completedAt; }
    public void setCompletedAt(Timestamp completedAt) { this.completedAt = completedAt; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    // Enriched getters/setters
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }

    public Integer getStaffUserId() { return staffUserId; }
    public void setStaffUserId(Integer staffUserId) { this.staffUserId = staffUserId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
}