package com.hotel.management.reports.housekeeping;

import com.hotel.management.reports.housekeeping.*;
import com.hotel.management.reports.housekeeping.HousekeepingReportRowMapper;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HousekeepingReportRepositoryImpl implements HousekeepingReportRepository {

    private final JdbcTemplate jdbc;

    private final HousekeepingReportRowMapper rowMapper = new HousekeepingReportRowMapper();

    public HousekeepingReportRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final String ADMIN_SQL = """
            SELECT
                st.task_id,
                u.name AS staff_name,
                r.room_number,
                hb.name AS branch_name,
                st.task_type,
                st.status,
                st.assigned_at,
                st.completed_at,
                TIMESTAMPDIFF(MINUTE, st.assigned_at, st.completed_at) AS time_taken_minutes
            FROM staff_tasks st
            JOIN users u ON u.user_id = st.staff_id
            JOIN rooms r ON r.room_id = st.room_id
            JOIN hotel_branches hb ON hb.branch_id = r.branch_id
            ORDER BY st.assigned_at DESC
            """;

    private static final String MANAGER_SQL = """
            SELECT
                st.task_id,
                u.name AS staff_name,
                r.room_number,
                hb.name AS branch_name,
                st.task_type,
                st.status,
                st.assigned_at,
                st.completed_at,
                TIMESTAMPDIFF(MINUTE, st.assigned_at, st.completed_at) AS time_taken_minutes
            FROM staff_tasks st
            JOIN users u ON u.user_id = st.staff_id
            JOIN rooms r ON r.room_id = st.room_id
            JOIN hotel_branches hb ON hb.branch_id = r.branch_id
            WHERE hb.manager_id = ?
            ORDER BY st.assigned_at DESC
            """;

    @Override
    public List<HousekeepingReportRowDto> getAdminReport() {
        return jdbc.query(ADMIN_SQL, rowMapper);
    }

    @Override
    public List<HousekeepingReportRowDto> getManagerReport(Integer managerUserId) {
        return jdbc.query(MANAGER_SQL, rowMapper, managerUserId);
    }

    @Override
    public HousekeepingSummaryDto getAdminSummary() {
        return jdbc.queryForObject("""
                SELECT
                    COUNT(*) AS totalTasks,
                    SUM(status = 'COMPLETED') AS completedTasks,
                    SUM(status = 'PENDING') AS pendingTasks,
                    SUM(status = 'IN_PROGRESS') AS inProgressTasks,
                    (SUM(status = 'COMPLETED') / COUNT(*)) * 100 AS completionPercentage,
                    AVG(TIMESTAMPDIFF(MINUTE, assigned_at, completed_at)) AS avgTime
                FROM staff_tasks
                """,
                (rs, rowNum) -> new HousekeepingSummaryDto(
                        rs.getInt("totalTasks"),
                        rs.getInt("completedTasks"),
                        rs.getInt("pendingTasks"),
                        rs.getInt("inProgressTasks"),
                        rs.getDouble("completionPercentage"),
                        rs.getDouble("avgTime")
                ));
    }

    @Override
    public HousekeepingSummaryDto getManagerSummary(Integer managerUserId) {
        return jdbc.queryForObject("""
                SELECT
                    COUNT(*) AS totalTasks,
                    SUM(status = 'COMPLETED') AS completedTasks,
                    SUM(status = 'PENDING') AS pendingTasks,
                    SUM(status = 'IN_PROGRESS') AS inProgressTasks,
                    (SUM(status = 'COMPLETED') / COUNT(*)) * 100 AS completionPercentage,
                    AVG(TIMESTAMPDIFF(MINUTE, assigned_at, completed_at)) AS avgTime
                FROM staff_tasks st
                JOIN rooms r ON r.room_id = st.room_id
                JOIN hotel_branches hb ON hb.branch_id = r.branch_id
                WHERE hb.manager_id = ?
                """,
                (rs, rowNum) -> new HousekeepingSummaryDto(
                        rs.getInt("totalTasks"),
                        rs.getInt("completedTasks"),
                        rs.getInt("pendingTasks"),
                        rs.getInt("inProgressTasks"),
                        rs.getDouble("completionPercentage"),
                        rs.getDouble("avgTime")
                ), managerUserId);
    }
}