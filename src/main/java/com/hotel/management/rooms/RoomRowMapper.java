package com.hotel.management.rooms;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomRowMapper implements RowMapper<Room> {
    @Override
    public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        room.setBranchId(rs.getInt("branch_id"));
        room.setTypeId(rs.getInt("type_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setPricePerNight(rs.getDouble("price_per_night"));
        room.setCapacity(rs.getInt("capacity"));
        room.setStatus(rs.getString("status"));
        room.setRoomPrimaryImage(rs.getString("room_primary_image"));
        room.setDescription(rs.getString("description"));
        room.setFloorNumber(rs.getInt("floor_number"));
        room.setLastCleaned(rs.getTimestamp("last_cleaned"));
        room.setCreatedAt(rs.getTimestamp("created_at"));
        room.setUpdatedAt(rs.getTimestamp("updated_at"));
        return room;
    }
}
