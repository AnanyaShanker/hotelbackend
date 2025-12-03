package com.hotel.management.reports.roomoccupancy;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomOccupancyRepositoryImpl implements RoomOccupancyRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RoomOccupancyRowMapper mapper = new RoomOccupancyRowMapper();

    public RoomOccupancyRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String BASE_QUERY = """
            SELECT 
                r.room_id,
                r.room_number,
                hb.name AS branch_name,
                rt.type_name,
                r.created_at,
                r.status AS current_status,

                -- total nights since room was created
                DATEDIFF(CURDATE(), r.created_at) AS total_available_nights,

                -- booked nights calculation
                COALESCE((
                    SELECT SUM(DATEDIFF(b.check_out_date, b.check_in_date))
                    FROM bookings b
                    WHERE b.room_id = r.room_id
                ), 0) AS booked_nights,

                -- occupancy %
                COALESCE((
                    (
                        SELECT SUM(DATEDIFF(b.check_out_date, b.check_in_date))
                        FROM bookings b
                        WHERE b.room_id = r.room_id
                    ) 
                    / NULLIF(DATEDIFF(CURDATE(), r.created_at), 0)
                ) * 100, 0) AS occupancy_percent,

                -- bookings count
                (SELECT COUNT(*) FROM bookings b WHERE b.room_id = r.room_id) AS total_bookings,

                -- revenue
                COALESCE((
                    SELECT SUM(b.total_price)
                    FROM bookings b
                    WHERE b.room_id = r.room_id
                ), 0) AS total_revenue

            FROM rooms r
            JOIN hotel_branches hb ON hb.branch_id = r.branch_id
            JOIN room_types rt ON rt.type_id = r.type_id
            """;

    @Override
    public List<RoomOccupancyDto> getOverallRoomOccupancyReport() {
        return jdbcTemplate.query(BASE_QUERY + " ORDER BY r.room_id", mapper);
    }

    @Override
    public List<RoomOccupancyDto> getRoomOccupancyByBranch(Integer branchId) {
        return jdbcTemplate.query(
                BASE_QUERY + " WHERE r.branch_id = ? ORDER BY r.room_id",
                mapper,
                branchId
        );
    }
}