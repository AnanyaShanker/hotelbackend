package com.hotel.management.roomfacilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RoomFacilitiesRepositoryImpl implements RoomFacilitiesRepository {

	@Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public RoomFacilities findById(int rfId) {
        String sql = "SELECT * FROM room_facilities WHERE rf_id = ?";
        return jdbcTemplate.queryForObject(sql, new RoomFacilitiesRowMapper(), rfId);
    }

    @Override
    public List<RoomFacilities> findAll() {
        String sql = "SELECT * FROM room_facilities";
        return jdbcTemplate.query(sql, new RoomFacilitiesRowMapper());
    }

    @Override
    public void save(RoomFacilities rf) {
        String sql = "INSERT INTO room_facilities (room_id, facility_name, description) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, rf.getRoomId(), rf.getFacilityName(), rf.getDescription());
    }

    @Override
    public void update(RoomFacilities rf) {
        String sql = "UPDATE room_facilities SET facility_name=?, description=? WHERE rf_id=?";
        jdbcTemplate.update(sql, rf.getFacilityName(), rf.getDescription(), rf.getRfId());
    }

    @Override
    public void softDelete(int rfId) {
        // Since status column doesn't exist, do hard delete instead
        String sql = "DELETE FROM room_facilities WHERE rf_id=?";
        jdbcTemplate.update(sql, rfId);
    }
}
