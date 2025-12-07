package com.hotel.management.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerDashboardService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Get complete dashboard data for a branch
     */
    public ManagerDashboardDTO getDashboardData(Integer branchId) {
        ManagerDashboardDTO dashboard = new ManagerDashboardDTO();

        // Fetch all dashboard components
        dashboard.setTodayStats(getTodayStats(branchId));
        dashboard.setMonthStats(getMonthStats(branchId));
        dashboard.setRecentActivity(getRecentActivity(branchId, 10));
        dashboard.setStaffList(getStaffSummary(branchId));
        dashboard.setTopRooms(getTopPerformingRooms(branchId, 5));
        dashboard.setPendingTasks(getPendingTasks(branchId));
        dashboard.setBranchInfo(getBranchInfo(branchId));

        return dashboard;
    }

    /**
     * Get today's statistics
     */
    public ManagerDashboardDTO.TodayStats getTodayStats(Integer branchId) {
        ManagerDashboardDTO.TodayStats stats = new ManagerDashboardDTO.TodayStats();

        String today = LocalDate.now().toString();

        // Total bookings today
        String sqlBookings = "SELECT COUNT(*) FROM bookings WHERE branch_id = ? AND DATE(created_at) = ?";
        Integer bookings = jdbcTemplate.queryForObject(sqlBookings, Integer.class, branchId, today);
        stats.setTotalBookings(bookings != null ? bookings : 0);

        // Revenue today
        String sqlRevenue = "SELECT COALESCE(SUM(total_price), 0) FROM bookings WHERE branch_id = ? AND DATE(created_at) = ? AND payment_status = 'PAID'";
        Double revenue = jdbcTemplate.queryForObject(sqlRevenue, Double.class, branchId, today);
        stats.setRevenueToday(revenue != null ? revenue : 0.0);

        // Occupied rooms
        String sqlOccupied = "SELECT COUNT(DISTINCT room_id) FROM bookings WHERE branch_id = ? AND booking_status = 'CONFIRMED' AND check_in_date <= NOW() AND check_out_date >= NOW()";
        Integer occupied = jdbcTemplate.queryForObject(sqlOccupied, Integer.class, branchId);
        stats.setOccupiedRooms(occupied != null ? occupied : 0);

        // Total rooms in branch
        String sqlTotalRooms = "SELECT COUNT(*) FROM rooms WHERE branch_id = ? AND status != 'BLOCKED'";
        Integer totalRooms = jdbcTemplate.queryForObject(sqlTotalRooms, Integer.class, branchId);
        int total = (totalRooms != null ? totalRooms : 0);

        // Available rooms
        stats.setAvailableRooms(total - stats.getOccupiedRooms());

        // Occupancy rate
        if (total > 0) {
            stats.setOccupancyRate((stats.getOccupiedRooms() * 100.0) / total);
        } else {
            stats.setOccupancyRate(0.0);
        }

        // Pending check-ins today
        String sqlCheckIns = "SELECT COUNT(*) FROM bookings WHERE branch_id = ? AND DATE(check_in_date) = ? AND booking_status = 'CONFIRMED'";
        Integer checkIns = jdbcTemplate.queryForObject(sqlCheckIns, Integer.class, branchId, today);
        stats.setPendingCheckIns(checkIns != null ? checkIns : 0);

        // Pending check-outs today
        String sqlCheckOuts = "SELECT COUNT(*) FROM bookings WHERE branch_id = ? AND DATE(check_out_date) = ? AND booking_status = 'CONFIRMED'";
        Integer checkOuts = jdbcTemplate.queryForObject(sqlCheckOuts, Integer.class, branchId, today);
        stats.setPendingCheckOuts(checkOuts != null ? checkOuts : 0);

        // New customers today
        String sqlNewCustomers = "SELECT COUNT(DISTINCT customer_id) FROM bookings WHERE branch_id = ? AND DATE(created_at) = ?";
        Integer newCustomers = jdbcTemplate.queryForObject(sqlNewCustomers, Integer.class, branchId, today);
        stats.setNewCustomers(newCustomers != null ? newCustomers : 0);

        return stats;
    }

    /**
     * Get this month's statistics
     */
    public ManagerDashboardDTO.MonthStats getMonthStats(Integer branchId) {
        ManagerDashboardDTO.MonthStats stats = new ManagerDashboardDTO.MonthStats();

        String thisMonth = LocalDate.now().withDayOfMonth(1).toString();

        // Total revenue this month
        String sqlRevenue = "SELECT COALESCE(SUM(total_price), 0) FROM bookings WHERE branch_id = ? AND created_at >= ? AND payment_status = 'PAID'";
        Double revenue = jdbcTemplate.queryForObject(sqlRevenue, Double.class, branchId, thisMonth);
        stats.setTotalRevenue(revenue != null ? revenue : 0.0);

        // Total bookings this month
        String sqlBookings = "SELECT COUNT(*) FROM bookings WHERE branch_id = ? AND created_at >= ?";
        Integer bookings = jdbcTemplate.queryForObject(sqlBookings, Integer.class, branchId, thisMonth);
        stats.setTotalBookings(bookings != null ? bookings : 0);

        // Total customers this month
        String sqlCustomers = "SELECT COUNT(DISTINCT customer_id) FROM bookings WHERE branch_id = ? AND created_at >= ?";
        Integer customers = jdbcTemplate.queryForObject(sqlCustomers, Integer.class, branchId, thisMonth);
        stats.setTotalCustomers(customers != null ? customers : 0);

        // Average occupancy rate (approximate)
        String sqlAvgOccupancy = "SELECT COALESCE(AVG(occupancy), 0) FROM (" +
                "SELECT DATE(created_at) as booking_date, " +
                "(COUNT(DISTINCT room_id) * 100.0 / (SELECT COUNT(*) FROM rooms WHERE branch_id = ?)) as occupancy " +
                "FROM bookings WHERE branch_id = ? AND created_at >= ? AND booking_status = 'CONFIRMED' " +
                "GROUP BY DATE(created_at)) as daily_occupancy";
        Double avgOccupancy = jdbcTemplate.queryForObject(sqlAvgOccupancy, Double.class, branchId, branchId, thisMonth);
        stats.setAverageOccupancyRate(avgOccupancy != null ? avgOccupancy : 0.0);

        // Revenue growth (compare with last month)
        String lastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1).toString();
        String lastMonthEnd = LocalDate.now().withDayOfMonth(1).minusDays(1).toString();
        String sqlLastMonthRevenue = "SELECT COALESCE(SUM(total_price), 0) FROM bookings WHERE branch_id = ? AND created_at >= ? AND created_at <= ? AND payment_status = 'PAID'";
        Double lastMonthRevenue = jdbcTemplate.queryForObject(sqlLastMonthRevenue, Double.class, branchId, lastMonth, lastMonthEnd);

        if (lastMonthRevenue != null && lastMonthRevenue > 0) {
            double growth = ((stats.getTotalRevenue() - lastMonthRevenue) / lastMonthRevenue) * 100;
            stats.setRevenueGrowth(growth);
        } else {
            stats.setRevenueGrowth(0.0);
        }

        return stats;
    }

    /**
     * Get recent activity
     */
    public List<ManagerDashboardDTO.RecentActivity> getRecentActivity(Integer branchId, int limit) {
        String sql = "SELECT 'BOOKING' as type, " +
                "CONCAT('New booking #', booking_id, ' by ', (SELECT name FROM users WHERE user_id = customer_id)) as description, " +
                "created_at as timestamp, " +
                "booking_status as status " +
                "FROM bookings WHERE branch_id = ? " +
                "ORDER BY created_at DESC LIMIT ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ManagerDashboardDTO.RecentActivity activity = new ManagerDashboardDTO.RecentActivity();
            activity.setType(rs.getString("type"));
            activity.setDescription(rs.getString("description"));
            activity.setTimestamp(rs.getTimestamp("timestamp").toString());
            activity.setStatus(rs.getString("status"));
            return activity;
        }, branchId, limit);
    }

    /**
     * Get staff summary for the branch
     */
    public List<ManagerDashboardDTO.StaffSummary> getStaffSummary(Integer branchId) {
        String sql = "SELECT s.staff_id, s.user_id, u.name, u.email, u.notes as department, s.status, " +
                "(SELECT COUNT(*) FROM staff_tasks WHERE staff_id = s.staff_id) as tasks_assigned, " +
                "(SELECT COUNT(*) FROM staff_tasks WHERE staff_id = s.staff_id AND status = 'COMPLETED') as tasks_completed, " +
                "(SELECT COUNT(*) FROM staff_tasks WHERE staff_id = s.staff_id AND status IN ('PENDING', 'IN_PROGRESS')) as tasks_pending " +
                "FROM staff s " +
                "JOIN users u ON s.user_id = u.user_id " +
                "WHERE s.hotel_id = ? AND s.status = 'AVAILABLE' " +
                "ORDER BY u.name";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ManagerDashboardDTO.StaffSummary staff = new ManagerDashboardDTO.StaffSummary();
            staff.setStaffId(rs.getInt("staff_id"));
            staff.setUserId(rs.getInt("user_id"));
            staff.setName(rs.getString("name"));
            staff.setEmail(rs.getString("email"));
            staff.setDepartment(rs.getString("department"));
            staff.setStatus(rs.getString("status"));
            staff.setTasksAssigned(rs.getInt("tasks_assigned"));
            staff.setTasksCompleted(rs.getInt("tasks_completed"));
            staff.setTasksPending(rs.getInt("tasks_pending"));
            return staff;
        }, branchId);
    }

    /**
     * Get top performing rooms
     */
    public List<ManagerDashboardDTO.RoomPerformance> getTopPerformingRooms(Integer branchId, int limit) {
        String sql = "SELECT r.room_id, r.room_number, rt.type_name as room_type, " +
                "COUNT(b.booking_id) as total_bookings, " +
                "COALESCE(SUM(b.total_price), 0) as total_revenue, " +
                "((COUNT(b.booking_id) * 100.0) / (SELECT COUNT(*) FROM bookings WHERE branch_id = ?)) as occupancy_rate " +
                "FROM rooms r " +
                "JOIN room_types rt ON r.type_id = rt.type_id " +
                "LEFT JOIN bookings b ON r.room_id = b.room_id AND b.booking_status = 'CONFIRMED' " +
                "WHERE r.branch_id = ? " +
                "GROUP BY r.room_id, r.room_number, rt.type_name " +
                "ORDER BY total_revenue DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ManagerDashboardDTO.RoomPerformance room = new ManagerDashboardDTO.RoomPerformance();
            room.setRoomId(rs.getInt("room_id"));
            room.setRoomNumber(rs.getString("room_number"));
            room.setRoomType(rs.getString("room_type"));
            room.setTotalBookings(rs.getInt("total_bookings"));
            room.setTotalRevenue(rs.getDouble("total_revenue"));
            room.setOccupancyRate(rs.getDouble("occupancy_rate"));
            return room;
        }, branchId, branchId, limit);
    }

    /**
     * Get pending tasks
     */
    public List<ManagerDashboardDTO.PendingTask> getPendingTasks(Integer branchId) {
        String sql = "SELECT st.task_id, st.staff_id, u.name as staff_name, st.room_id, r.room_number, " +
                "st.task_type, st.status, st.assigned_at, st.remarks " +
                "FROM staff_tasks st " +
                "JOIN staff s ON st.staff_id = s.staff_id " +
                "JOIN users u ON s.user_id = u.user_id " +
                "LEFT JOIN rooms r ON st.room_id = r.room_id " +
                "WHERE s.hotel_id = ? AND st.status IN ('PENDING', 'IN_PROGRESS') " +
                "ORDER BY st.assigned_at DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ManagerDashboardDTO.PendingTask task = new ManagerDashboardDTO.PendingTask();
            task.setTaskId(rs.getInt("task_id"));
            task.setStaffId(rs.getInt("staff_id"));
            task.setStaffName(rs.getString("staff_name"));
            task.setRoomId((Integer) rs.getObject("room_id"));
            task.setRoomNumber(rs.getString("room_number"));
            task.setTaskType(rs.getString("task_type"));
            task.setStatus(rs.getString("status"));
            task.setAssignedAt(rs.getTimestamp("assigned_at").toString());
            task.setRemarks(rs.getString("remarks"));
            return task;
        }, branchId);
    }

    /**
     * Get branch information
     */
    public ManagerDashboardDTO.BranchInfo getBranchInfo(Integer branchId) {
        String sql = "SELECT hb.branch_id, hb.name as branch_name, hb.location, hb.total_rooms, " +
                "(SELECT COUNT(*) FROM staff WHERE hotel_id = hb.branch_id) as total_staff, " +
                "(SELECT name FROM users WHERE user_id = hb.manager_id) as manager_name " +
                "FROM hotel_branches hb " +
                "WHERE hb.branch_id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            ManagerDashboardDTO.BranchInfo info = new ManagerDashboardDTO.BranchInfo();
            info.setBranchId(rs.getInt("branch_id"));
            info.setBranchName(rs.getString("branch_name"));
            info.setLocation(rs.getString("location"));
            info.setTotalRooms(rs.getInt("total_rooms"));
            info.setTotalStaff(rs.getInt("total_staff"));
            info.setManagerName(rs.getString("manager_name"));
            return info;
        }, branchId);
    }
}

