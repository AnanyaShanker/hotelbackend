package com.hotel.management.feedback;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedbackDisplayRowMapper implements RowMapper<FeedbackDisplayDto> {

    @Override
    public FeedbackDisplayDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        FeedbackDisplayDto dto = new FeedbackDisplayDto();
        dto.setFeedbackId(rs.getInt("feedback_id"));
        dto.setCustomerName(rs.getString("customer_name"));
        dto.setItemName(rs.getString("item_name"));
        dto.setRating(rs.getInt("rating"));
        dto.setComments(rs.getString("comments"));
        return dto;
    }
}
