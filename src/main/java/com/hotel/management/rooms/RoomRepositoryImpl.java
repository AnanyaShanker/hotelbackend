package com.hotel.management.rooms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RoomRepositoryImpl implements RoomRepository {

	@Autowired
    private JdbcTemplate jdbcTemplate;

    
    @Override
    public Room save(Room room) {
        String sql = "INSERT INTO rooms (branch_id, type_id, room_number, price_per_night, capacity, status, room_primary_image, description, floor_number) VALUES (?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                room.getBranchId(),
                room.getTypeId(),
                room.getRoomNumber(),
                room.getPricePerNight(),
                room.getCapacity(),
                room.getStatus(),
                room.getRoomPrimaryImage(),
                room.getDescription(),
                room.getFloorNumber()
        );
        return room;
    }

    @Override
    public List<Room> findAll() {
        return jdbcTemplate.query("SELECT * FROM rooms", new RoomRowMapper());
    }
    
    @Override
	public List<Room> findAllAvailable() {
    	 return jdbcTemplate.query("SELECT * FROM rooms WHERE status != 'BLOCKED'", new RoomRowMapper());
	}

    @Override
    public Room findById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE room_id = ? AND status != 'BLOCKED'";
        return jdbcTemplate.queryForObject(sql, new RoomRowMapper(), roomId);
    }

    @Override
    public List<Room> findByBranch(int branchId) {
        String sql = "SELECT * FROM rooms WHERE branch_id = ? AND status != 'BLOCKED'";
        return jdbcTemplate.query(sql, new RoomRowMapper(), branchId);
    }

    @Override
    public List<Room> findByType(int typeId) {
        String sql = "SELECT * FROM rooms WHERE type_id = ? AND status != 'BLOCKED'";
        return jdbcTemplate.query(sql, new RoomRowMapper(), typeId);
    }

    @Override
    public List<Room> findAvailableRooms(int branchId, int typeId) {
        String sql = "SELECT * FROM rooms WHERE branch_id = ? AND type_id = ? AND status = 'AVAILABLE'";
        return jdbcTemplate.query(sql, new RoomRowMapper(), branchId, typeId);
    }

    @Override
    public Room update(Room room) {
        String sql = "UPDATE rooms SET branch_id=?, type_id=?, room_number=?, price_per_night=?, capacity=?, status=?, room_primary_image=?, description=?, floor_number=? WHERE room_id=?";
        jdbcTemplate.update(sql,
                room.getBranchId(),
                room.getTypeId(),
                room.getRoomNumber(),
                room.getPricePerNight(),
                room.getCapacity(),
                room.getStatus(),
                room.getRoomPrimaryImage(),
                room.getDescription(),
                room.getFloorNumber(),
                room.getRoomId()
        );
        return room;
    }

    @Override
    public void softDelete(int roomId) {
        jdbcTemplate.update("UPDATE rooms SET status='BLOCKED' WHERE room_id=?", roomId);
    }

    @Override
    public void updateStatus(int roomId, String status) {
        jdbcTemplate.update("UPDATE rooms SET status=? WHERE room_id=?", status, roomId);
    }

	
}
