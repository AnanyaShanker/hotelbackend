package com.hotel.management.roomtypes;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RoomTypeRowMapper implements RowMapper<RoomTypes> {
    @Override
    public RoomTypes mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RoomTypes(
            rs.getInt("type_id"),
            rs.getString("type_name"),
            rs.getString("description")
        );
    }
}
