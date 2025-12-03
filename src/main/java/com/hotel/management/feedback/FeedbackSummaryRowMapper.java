package com.hotel.management.feedback;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedbackSummaryRowMapper implements RowMapper<FeedbackSummaryDto> {

    @Override
    public FeedbackSummaryDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        FeedbackSummaryDto dto = new FeedbackSummaryDto();

        dto.setFeedbackId(rs.getInt("feedback_id"));
        dto.setCustomerName(rs.getString("customer_name"));
        dto.setBranchName(rs.getString("branch_name"));
        dto.setItemName(rs.getString("item_name"));
        dto.setBookingOrFacilityId(rs.getInt("booking_or_facility_id"));
        dto.setRating(rs.getInt("rating"));
        dto.setComments(rs.getString("comments"));

        return dto;
    }
}