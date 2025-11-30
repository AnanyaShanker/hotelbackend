package com.hotel.management.supporttickets;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class SupportTicketRepositoryImpl implements SupportTicketRepository {

	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insert;
	private final SupportTicketRowMapper rowMapper = new SupportTicketRowMapper();

	private static final String TABLE = "hotel_management.support_tickets";

	public SupportTicketRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("support_tickets") 
				.usingGeneratedKeyColumns("ticket_id");
	}

	@Override
	public Integer addTicket(SupportTicket ticket) {
		Map<String, Object> params = new HashMap<>();
		params.put("customer_id", ticket.getCustomerId());
		params.put("assigned_staff_id", ticket.getAssignedStaffId());
		params.put("booking_id", ticket.getBookingId());
		params.put("facility_booking_id", ticket.getFacilityBookingId());
		params.put("subject", ticket.getSubject());
		params.put("category", ticket.getCategory());
		params.put("status", ticket.getStatus() == null ? "OPEN" : ticket.getStatus());
		params.put("details", ticket.getDetails());
		Number key = insert.executeAndReturnKey(params);
		return key.intValue();
	}

	@Override
	public Optional<SupportTicket> getTicketById(Integer ticketId) {
		try {
			SupportTicket t = jdbcTemplate.queryForObject("SELECT * FROM " + TABLE + " WHERE ticket_id = ?", rowMapper,
					ticketId);
			return Optional.ofNullable(t);
		} catch (EmptyResultDataAccessException ex) {
			return Optional.empty();
		}
	}

	@Override
	public List<SupportTicket> getAllTickets() {
		return jdbcTemplate.query("SELECT * FROM " + TABLE + " ORDER BY ticket_id DESC", rowMapper);
	}

	@Override
	public List<SupportTicket> getTicketsByCustomer(Integer customerId) {
		return jdbcTemplate.query("SELECT * FROM " + TABLE + " WHERE customer_id = ? ORDER BY ticket_id DESC",
				rowMapper, customerId);
	}

	@Override
	public List<SupportTicket> getTicketsByStatus(String status) {
		return jdbcTemplate.query("SELECT * FROM " + TABLE + " WHERE status = ? ORDER BY ticket_id DESC", rowMapper,
				status);
	}

	@Override
	public List<SupportTicket> getTicketsByAssignedStaff(Integer staffId) {
		return jdbcTemplate.query("SELECT * FROM " + TABLE + " WHERE assigned_staff_id = ? ORDER BY ticket_id DESC",
				rowMapper, staffId);
	}

	@Override
	public void updateTicket(Integer ticketId, SupportTicket ticket) {
		String sql = "UPDATE " + TABLE
				+ " SET subject=?, category=?, details=?, assigned_staff_id=?, booking_id=?, facility_booking_id=?, updated_at = NOW() WHERE ticket_id = ?";
		jdbcTemplate.update(sql, ticket.getSubject(), ticket.getCategory(), ticket.getDetails(),
				ticket.getAssignedStaffId(), ticket.getBookingId(), ticket.getFacilityBookingId(), ticketId);
	}

	@Override
	public void changeStatus(Integer ticketId, String status) {
		String sql = "UPDATE " + TABLE + " SET status = ?, updated_at = NOW() WHERE ticket_id = ?";
		jdbcTemplate.update(sql, status, ticketId);
	}

	@Override
	public void assignStaff(Integer ticketId, Integer staffId) {
		String sql = "UPDATE " + TABLE + " SET assigned_staff_id = ?, updated_at = NOW() WHERE ticket_id = ?";
		jdbcTemplate.update(sql, staffId, ticketId);
	}

//	@Override
//	public void deleteTicket(Integer ticketId) {
//		// You said no hard delete — keep this unimplemented or perform soft-delete by
//		// changing status/flag.
//		// Example (soft-delete): jdbcTemplate.update("UPDATE " + TABLE + " SET status =
//		// 'CLOSED', updated_at = NOW() WHERE ticket_id = ?", ticketId);
//		jdbcTemplate.update("UPDATE " + TABLE + " SET status = 'CLOSED', updated_at = NOW() WHERE ticket_id = ?",
//				ticketId);
//	}
}