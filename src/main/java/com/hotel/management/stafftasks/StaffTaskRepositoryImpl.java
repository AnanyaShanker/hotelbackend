package com.hotel.management.stafftasks;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
 
@Repository
public class StaffTaskRepositoryImpl implements StaffTaskRepository {
 
	@Autowired
    private JdbcTemplate jdbcTemplate;
    private final StaffTaskRowMapper rowMapper = new StaffTaskRowMapper();
 
 
    @Override
    public int save(StaffTask task) {
        String sql = "INSERT INTO staff_tasks (staff_id, room_id, task_type, status, remarks) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, task.getStaffId(), task.getRoomId(), task.getTaskType(), task.getStatus(), task.getRemarks());
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }
 
    @Override
    public Optional<StaffTask> findById(int taskId) {
        String sql = "SELECT * FROM staff_tasks WHERE task_id = ?";
        List<StaffTask> list = jdbcTemplate.query(sql, rowMapper, taskId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
 
    @Override
    public List<StaffTask> findByStaff(int staffId) {
        return jdbcTemplate.query("SELECT * FROM staff_tasks WHERE staff_id = ?", rowMapper, staffId);
    }
 
    @Override
    public List<StaffTask> findByRoom(int roomId) {
        return jdbcTemplate.query("SELECT * FROM staff_tasks WHERE room_id = ?", rowMapper, roomId);
    }
 
    @Override
    public List<StaffTask> findAll() {
        return jdbcTemplate.query("SELECT * FROM staff_tasks", rowMapper);
    }
 
    @Override
    public int updateStatus(int taskId, String status, String remarks) {
        String sql = "UPDATE staff_tasks SET status = ?, completed_at = CASE WHEN ? = 'COMPLETED' THEN CURRENT_TIMESTAMP ELSE completed_at END, remarks = ? WHERE task_id = ?";
        return jdbcTemplate.update(sql, status, status, remarks, taskId);
    }
}