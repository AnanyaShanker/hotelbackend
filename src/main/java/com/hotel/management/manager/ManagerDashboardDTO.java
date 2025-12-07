package com.hotel.management.manager;



import java.util.List;

/**
 * DTO for Manager Dashboard data
 */
public class ManagerDashboardDTO {

    // Today's statistics
    private TodayStats todayStats;

    // Month statistics
    private MonthStats monthStats;

    // Recent activity
    private List<RecentActivity> recentActivity;

    // Staff list
    private List<StaffSummary> staffList;

    // Top performing rooms
    private List<RoomPerformance> topRooms;

    // Pending tasks
    private List<PendingTask> pendingTasks;

    // Branch information
    private BranchInfo branchInfo;

    // Getters and Setters
    public TodayStats getTodayStats() {
        return todayStats;
    }

    public void setTodayStats(TodayStats todayStats) {
        this.todayStats = todayStats;
    }

    public MonthStats getMonthStats() {
        return monthStats;
    }

    public void setMonthStats(MonthStats monthStats) {
        this.monthStats = monthStats;
    }

    public List<RecentActivity> getRecentActivity() {
        return recentActivity;
    }

    public void setRecentActivity(List<RecentActivity> recentActivity) {
        this.recentActivity = recentActivity;
    }

    public List<StaffSummary> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<StaffSummary> staffList) {
        this.staffList = staffList;
    }

    public List<RoomPerformance> getTopRooms() {
        return topRooms;
    }

    public void setTopRooms(List<RoomPerformance> topRooms) {
        this.topRooms = topRooms;
    }

    public List<PendingTask> getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(List<PendingTask> pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public BranchInfo getBranchInfo() {
        return branchInfo;
    }

    public void setBranchInfo(BranchInfo branchInfo) {
        this.branchInfo = branchInfo;
    }

    // Inner classes for nested data

    public static class TodayStats {
        private int totalBookings;
        private double revenueToday;
        private int occupiedRooms;
        private int availableRooms;
        private double occupancyRate;
        private int pendingCheckIns;
        private int pendingCheckOuts;
        private int newCustomers;

        // Getters and Setters
        public int getTotalBookings() { return totalBookings; }
        public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }

        public double getRevenueToday() { return revenueToday; }
        public void setRevenueToday(double revenueToday) { this.revenueToday = revenueToday; }

        public int getOccupiedRooms() { return occupiedRooms; }
        public void setOccupiedRooms(int occupiedRooms) { this.occupiedRooms = occupiedRooms; }

        public int getAvailableRooms() { return availableRooms; }
        public void setAvailableRooms(int availableRooms) { this.availableRooms = availableRooms; }

        public double getOccupancyRate() { return occupancyRate; }
        public void setOccupancyRate(double occupancyRate) { this.occupancyRate = occupancyRate; }

        public int getPendingCheckIns() { return pendingCheckIns; }
        public void setPendingCheckIns(int pendingCheckIns) { this.pendingCheckIns = pendingCheckIns; }

        public int getPendingCheckOuts() { return pendingCheckOuts; }
        public void setPendingCheckOuts(int pendingCheckOuts) { this.pendingCheckOuts = pendingCheckOuts; }

        public int getNewCustomers() { return newCustomers; }
        public void setNewCustomers(int newCustomers) { this.newCustomers = newCustomers; }
    }

    public static class MonthStats {
        private double totalRevenue;
        private int totalBookings;
        private double averageOccupancyRate;
        private int totalCustomers;
        private double revenueGrowth; // Percentage compared to last month

        // Getters and Setters
        public double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }

        public int getTotalBookings() { return totalBookings; }
        public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }

        public double getAverageOccupancyRate() { return averageOccupancyRate; }
        public void setAverageOccupancyRate(double averageOccupancyRate) { this.averageOccupancyRate = averageOccupancyRate; }

        public int getTotalCustomers() { return totalCustomers; }
        public void setTotalCustomers(int totalCustomers) { this.totalCustomers = totalCustomers; }

        public double getRevenueGrowth() { return revenueGrowth; }
        public void setRevenueGrowth(double revenueGrowth) { this.revenueGrowth = revenueGrowth; }
    }

    public static class RecentActivity {
        private String type; // BOOKING, PAYMENT, CHECKOUT, etc.
        private String description;
        private String timestamp;
        private String status;

        // Getters and Setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class StaffSummary {
        private Integer staffId;
        private Integer userId;
        private String name;
        private String email;
        private String department;
        private String status;
        private int tasksAssigned;
        private int tasksCompleted;
        private int tasksPending;

        // Getters and Setters
        public Integer getStaffId() { return staffId; }
        public void setStaffId(Integer staffId) { this.staffId = staffId; }

        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public int getTasksAssigned() { return tasksAssigned; }
        public void setTasksAssigned(int tasksAssigned) { this.tasksAssigned = tasksAssigned; }

        public int getTasksCompleted() { return tasksCompleted; }
        public void setTasksCompleted(int tasksCompleted) { this.tasksCompleted = tasksCompleted; }

        public int getTasksPending() { return tasksPending; }
        public void setTasksPending(int tasksPending) { this.tasksPending = tasksPending; }
    }

    public static class RoomPerformance {
        private Integer roomId;
        private String roomNumber;
        private String roomType;
        private int totalBookings;
        private double totalRevenue;
        private double occupancyRate;

        // Getters and Setters
        public Integer getRoomId() { return roomId; }
        public void setRoomId(Integer roomId) { this.roomId = roomId; }

        public String getRoomNumber() { return roomNumber; }
        public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

        public String getRoomType() { return roomType; }
        public void setRoomType(String roomType) { this.roomType = roomType; }

        public int getTotalBookings() { return totalBookings; }
        public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }

        public double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }

        public double getOccupancyRate() { return occupancyRate; }
        public void setOccupancyRate(double occupancyRate) { this.occupancyRate = occupancyRate; }
    }

    public static class PendingTask {
        private Integer taskId;
        private Integer staffId;
        private String staffName;
        private Integer roomId;
        private String roomNumber;
        private String taskType;
        private String status;
        private String assignedAt;
        private String remarks;

        // Getters and Setters
        public Integer getTaskId() { return taskId; }
        public void setTaskId(Integer taskId) { this.taskId = taskId; }

        public Integer getStaffId() { return staffId; }
        public void setStaffId(Integer staffId) { this.staffId = staffId; }

        public String getStaffName() { return staffName; }
        public void setStaffName(String staffName) { this.staffName = staffName; }

        public Integer getRoomId() { return roomId; }
        public void setRoomId(Integer roomId) { this.roomId = roomId; }

        public String getRoomNumber() { return roomNumber; }
        public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

        public String getTaskType() { return taskType; }
        public void setTaskType(String taskType) { this.taskType = taskType; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getAssignedAt() { return assignedAt; }
        public void setAssignedAt(String assignedAt) { this.assignedAt = assignedAt; }

        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }

    public static class BranchInfo {
        private Integer branchId;
        private String branchName;
        private String location;
        private int totalRooms;
        private int totalStaff;
        private String managerName;

        // Getters and Setters
        public Integer getBranchId() { return branchId; }
        public void setBranchId(Integer branchId) { this.branchId = branchId; }

        public String getBranchName() { return branchName; }
        public void setBranchName(String branchName) { this.branchName = branchName; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public int getTotalRooms() { return totalRooms; }
        public void setTotalRooms(int totalRooms) { this.totalRooms = totalRooms; }

        public int getTotalStaff() { return totalStaff; }
        public void setTotalStaff(int totalStaff) { this.totalStaff = totalStaff; }

        public String getManagerName() { return managerName; }
        public void setManagerName(String managerName) { this.managerName = managerName; }
    }
}

