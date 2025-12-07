package com.hotel.management.stafftasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Repository
public class StaffTaskRepositoryImpl implements StaffTaskRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final StaffTaskRowMapper rowMapper = new StaffTaskRowMapper();
    private final SimpleJdbcInsert insert;

    @Autowired
    public StaffTaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("staff_tasks")
                .usingGeneratedKeyColumns("task_id");
    }

    @Override
    public int save(StaffTask task) {
        // If assignedAt not provided, set to NOW() in DB. We'll do two paths:
        if (task.getAssignedAt() == null) {
            String sql = "INSERT INTO staff_tasks (staff_id, room_id, task_type, status, remarks, assigned_at) VALUES (?, ?, ?, ?, ?, NOW())";
            jdbcTemplate.update(sql,
                    task.getStaffId(),
                    task.getRoomId(),
                    task.getTaskType(),
                    task.getStatus(),
                    task.getRemarks());
            return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        } else {
            // assignedAt provided by caller:
            Map<String, Object> params = new HashMap<>();
            params.put("staff_id", task.getStaffId());
            params.put("room_id", task.getRoomId());
            params.put("task_type", task.getTaskType());
            params.put("status", task.getStatus());
            params.put("remarks", task.getRemarks());
            params.put("assigned_at", task.getAssignedAt());
            Number key = insert.executeAndReturnKey(params);
            return key.intValue();
        }
    }

    @Override
    public Optional<StaffTask> findById(int taskId) {
        String sql = "SELECT st.* FROM staff_tasks st WHERE st.task_id = ?";
        List<StaffTask> list = jdbcTemplate.query(sql, rowMapper, taskId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<StaffTask> findByStaff(int staffId) {
        String sql = "SELECT st.*, u.name as staff_name, u.user_id, r.room_number " +
                     "FROM staff_tasks st " +
                     "LEFT JOIN staff s ON st.staff_id = s.staff_id " +
                     "LEFT JOIN users u ON s.user_id = u.user_id " +
                     "LEFT JOIN rooms r ON st.room_id = r.room_id " +
                     "WHERE st.staff_id = ? ORDER BY st.task_id DESC";
        return jdbcTemplate.query(sql, rowMapper, staffId);
    }

    @Override
    public List<StaffTask> findByRoom(int roomId) {
        String sql = "SELECT st.*, u.name as staff_name, u.user_id, r.room_number " +
                     "FROM staff_tasks st " +
                     "LEFT JOIN staff s ON st.staff_id = s.staff_id " +
                     "LEFT JOIN users u ON s.user_id = u.user_id " +
                     "LEFT JOIN rooms r ON st.room_id = r.room_id " +
                     "WHERE st.room_id = ? ORDER BY st.task_id DESC";
        return jdbcTemplate.query(sql, rowMapper, roomId);
    }

    @Override
    public List<StaffTask> findAll() {
        String sql = "SELECT st.*, u.name as staff_name, u.user_id, r.room_number " +
                     "FROM staff_tasks st " +
                     "LEFT JOIN staff s ON st.staff_id = s.staff_id " +
                     "LEFT JOIN users u ON s.user_id = u.user_id " +
                     "LEFT JOIN rooms r ON st.room_id = r.room_id " +
                     "ORDER BY st.task_id DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public int updateStatus(int taskId, String status, String remarks) {
        String sql = "UPDATE staff_tasks SET status = ?, remarks = ?, " +
                     "completed_at = CASE WHEN ? = 'COMPLETED' THEN CURRENT_TIMESTAMP ELSE completed_at END " +
                     "WHERE task_id = ?";
        return jdbcTemplate.update(sql, status, remarks, status, taskId);
    }

    @Override
    public List<StaffTask> findByHotel(int hotelId) {
        // Join staff -> tasks and include staff user name and room number
        String sql = "SELECT st.*, u.name as staff_name, u.user_id, r.room_number " +
                     "FROM staff_tasks st " +
                     "INNER JOIN staff s ON st.staff_id = s.staff_id " +
                     "LEFT JOIN users u ON s.user_id = u.user_id " +
                     "LEFT JOIN rooms r ON st.room_id = r.room_id " +
                     "WHERE s.hotel_id = ? " +
                     "ORDER BY st.task_id DESC";
        return jdbcTemplate.query(sql, rowMapper, hotelId);
    }
}

