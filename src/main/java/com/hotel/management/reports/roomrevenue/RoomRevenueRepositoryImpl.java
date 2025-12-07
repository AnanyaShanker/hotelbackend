package com.hotel.management.reports.roomrevenue;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.RowMapper;

import org.springframework.stereotype.Repository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Repository

public class RoomRevenueRepositoryImpl implements RoomRevenueRepository {

    @Autowired

    private JdbcTemplate jdbcTemplate;

    private final RowMapper<RoomRevenueDto> mapper = new RoomRevenueRowMapper();

    // UPDATED — revenue counts ONLY "COMPLETED" bookings

    private static final String ADMIN_SQL = """

            SELECT

                rt.type_name,

                COALESCE(SUM(b.total_price), 0) AS revenue,

                COALESCE(COUNT(b.booking_id), 0) AS total_bookings,

                COALESCE(SUM(DATEDIFF(b.check_out_date, b.check_in_date)), 0) AS booked_nights,

                COALESCE(

                    CASE WHEN COUNT(b.booking_id) = 0 THEN 0

                         ELSE ROUND(SUM(b.total_price) / COUNT(b.booking_id), 2)

                    END, 0

                ) AS avg_revenue

            FROM room_types rt

            LEFT JOIN rooms r ON r.type_id = rt.type_id

            LEFT JOIN bookings b ON b.room_id = r.room_id

            WHERE b.booking_status = 'COMPLETED'

            GROUP BY rt.type_id, rt.type_name

            ORDER BY rt.type_name;

    """;

    private static final String MANAGER_SQL = """

            SELECT

                rt.type_name,

                COALESCE(SUM(b.total_price), 0) AS revenue,

                COALESCE(COUNT(b.booking_id), 0) AS total_bookings,

                COALESCE(SUM(DATEDIFF(b.check_out_date, b.check_in_date)), 0) AS booked_nights,

                COALESCE(

                    CASE WHEN COUNT(b.booking_id) = 0 THEN 0

                         ELSE ROUND(SUM(b.total_price) / COUNT(b.booking_id), 2)

                    END, 0

                ) AS avg_revenue

            FROM room_types rt

            LEFT JOIN rooms r ON r.type_id = rt.type_id

            LEFT JOIN bookings b ON b.room_id = r.room_id

            LEFT JOIN hotel_branches hb ON hb.branch_id = r.branch_id

            WHERE hb.manager_id = ? AND b.booking_status = 'COMPLETED'

            GROUP BY rt.type_id, rt.type_name

            ORDER BY rt.type_name;

    """;

    @Override

    public List<RoomRevenueDto> getRevenueByRoomType() {

        return jdbcTemplate.query(ADMIN_SQL, mapper);

    }

    @Override

    public List<RoomRevenueDto> getRevenueByRoomTypeForManager(Integer managerUserId) {

        return jdbcTemplate.query(MANAGER_SQL, mapper, managerUserId);

    }

}
 