package com.hotel.management.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StaffRepositoryImpl implements StaffRepository {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private StaffRowMapper rowMapper = new StaffRowMapper();

    @Override
    public int save(Staff staff) {
        String sql = "INSERT INTO staff (user_id, hotel_id, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, staff.getUserId(), staff.getHotelId(), staff.getStatus());
        Integer generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        return generatedId == null ? 0 : generatedId;
    }

    @Override
    public Optional<Staff> findById(int staffId) {
        String sql = "SELECT staff_id, user_id, hotel_id, status FROM staff WHERE staff_id = ?";
        List<Staff> list = jdbcTemplate.query(sql, rowMapper, staffId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<Staff> findByUserId(int userId) {
        String sql = "SELECT staff_id, user_id, hotel_id, status FROM staff WHERE user_id = ?";
        List<Staff> list = jdbcTemplate.query(sql, rowMapper, userId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<Staff> findAll(Integer hotelId, String status) {
        String base = "SELECT staff_id, user_id, hotel_id, status FROM staff WHERE 1=1";
        StringBuilder sb = new StringBuilder(base);
        new Object();
        new Object();
        new Object();

        // Build dynamic query safely
        new Object();
        new Object();

        // Simple dynamic filtering
        if (hotelId != null && status != null) {
            String sql = base + " AND hotel_id = ? AND status = ?";
            return jdbcTemplate.query(sql, rowMapper, hotelId, status);
        } else if (hotelId != null) {
            String sql = base + " AND hotel_id = ?";
            return jdbcTemplate.query(sql, rowMapper, hotelId);
        } else if (status != null) {
            String sql = base + " AND status = ?";
            return jdbcTemplate.query(sql, rowMapper, status);
        } else {
            String sql = base;
            return jdbcTemplate.query(sql, rowMapper);
        }
    }

    @Override
    public int updateHotel(int staffId, int hotelId) {
        String sql = "UPDATE staff SET hotel_id = ? WHERE staff_id = ?";
        return jdbcTemplate.update(sql, hotelId, staffId);
    }

    @Override
    public int updateStatus(int staffId, String status) {
        String sql = "UPDATE staff SET status = ? WHERE staff_id = ?";
        return jdbcTemplate.update(sql, status, staffId);
    }

    @Override
    public boolean existsById(int staffId) {
        String sql = "SELECT COUNT(*) FROM staff WHERE staff_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, staffId);
        return count != null && count > 0;
    }

    // Find staff by staff_identifier
    @Override
    public Optional<Staff> findByStaffIdentifier(String staffIdentifier) {
        String sql = "SELECT * FROM staff WHERE staff_identifier = ?";
        List<Staff> list = jdbcTemplate.query(sql, rowMapper, staffIdentifier);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
}
