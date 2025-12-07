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
    	 return jdbcTemplate.query("SELECT * FROM rooms WHERE status != 'BLOCKED' and status != 'MAINTENANCE'", new RoomRowMapper());
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
    public List<Room> findAvailableRoomsForDates(int branchId, int typeId, java.sql.Timestamp checkIn, java.sql.Timestamp checkOut) {
        // Query to find rooms that are NOT booked during the requested date range
        // Two date ranges overlap if: (start1 < end2) AND (end1 > start2)
        // This is the standard interval overlap formula

        String sql = "SELECT r.* FROM rooms r " +
                     "WHERE r.branch_id = ? " +
                     "AND r.type_id = ? " +
                     "AND r.status != 'BLOCKED' " +  // Exclude blocked/deleted rooms
                     "AND r.room_id NOT IN ( " +
                     "    SELECT b.room_id FROM bookings b " +
                     "    WHERE b.booking_status IN ('CONFIRMED', 'COMPLETED') " +  // Only active bookings
                     "    AND b.check_in_date < ? " +  // Existing booking starts before new checkout
                     "    AND b.check_out_date > ? " + // Existing booking ends after new checkin
                     ")";

        // The overlap logic:
        // A room is unavailable if there's ANY booking where:
        // - Booking check-in < Requested check-out (booking starts before we leave)
        // - Booking check-out > Requested check-in (booking ends after we arrive)

        return jdbcTemplate.query(sql, new RoomRowMapper(),
            branchId, typeId,
            checkOut,  // Parameter for: b.check_in_date < ?
            checkIn    // Parameter for: b.check_out_date > ?
        );
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
