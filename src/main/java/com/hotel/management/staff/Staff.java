package com.hotel.management.staff;

import java.sql.Date;

public class Staff {
    private Integer staffId;
    private Integer userId;
    private Integer hotelId;
    private String status;

    //  Additional staff fields
    private String staffIdentifier;  // Unique staff ID (e.g., ST-MUM-001)
    private String department;       // Department (e.g., Housekeeping, Front Desk)
    private Date hireDate;           // Date when staff was hired
    private Integer reportsTo;       // Manager staff_id this staff reports to

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getHotelId() { return hotelId; }
    public void setHotelId(Integer hotelId) { this.hotelId = hotelId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    //Getters and Setters for new fields
    public String getStaffIdentifier() { return staffIdentifier; }
    public void setStaffIdentifier(String staffIdentifier) { this.staffIdentifier = staffIdentifier; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }

    public Integer getReportsTo() { return reportsTo; }
    public void setReportsTo(Integer reportsTo) { this.reportsTo = reportsTo; }
}

