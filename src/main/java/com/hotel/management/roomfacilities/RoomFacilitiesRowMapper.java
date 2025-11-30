package com.hotel.management.roomfacilities;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomFacilitiesRowMapper implements RowMapper<RoomFacilities> {
    @Override
    public RoomFacilities mapRow(ResultSet rs, int rowNum) throws SQLException {
        RoomFacilities rf = new RoomFacilities();
        rf.setRfId(rs.getInt("rf_id"));
        rf.setRoomId(rs.getInt("room_id"));
        rf.setFacilityName(rs.getString("facility_name"));
        rf.setDescription(rs.getString("description"));
        rf.setStatus(rs.getString("status"));
        return rf;
    }
}
