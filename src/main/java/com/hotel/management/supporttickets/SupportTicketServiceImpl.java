package com.hotel.management.supporttickets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupportTicketServiceImpl implements SupportTicketService {

	@Autowired
	private SupportTicketRepository supportTicketRepository;

	// Utility: convert DTO -> entity
	private SupportTicket toEntity(SupportTicketDto dto) {
		if (dto == null)
			return null;
		SupportTicket t = new SupportTicket();
		t.setTicketId(dto.getTicketId());
		t.setCustomerId(dto.getCustomerId());
		t.setAssignedStaffId(dto.getAssignedStaffId());
		t.setBookingId(dto.getBookingId());
		t.setFacilityBookingId(dto.getFacilityBookingId());
		t.setSubject(dto.getSubject());
		t.setCategory(dto.getCategory());
		t.setStatus(dto.getStatus());
		t.setDetails(dto.getDetails());
		t.setCreatedAt(dto.getCreatedAt());
		t.setUpdatedAt(dto.getUpdatedAt());
		return t;
	}

	// Utility: convert entity -> DTO
	private SupportTicketDto toDto(SupportTicket t) {
		if (t == null)
			return null;
		return new SupportTicketDto(t.getTicketId(), t.getCustomerId(), t.getAssignedStaffId(), t.getBookingId(),
				t.getFacilityBookingId(), t.getSubject(), t.getCategory(), t.getStatus(), t.getDetails(),
				t.getCreatedAt(), t.getUpdatedAt());
	}

	@Override
	public SupportTicketDto createTicket(SupportTicketDto dto) {
		// Basic validation: ensure customerId present
		if (dto.getCustomerId() == null) {
			throw new IllegalArgumentException("customerId is required");
		}

		// Ensure only one of bookingId / facilityBookingId is set (recommended)
		if (dto.getBookingId() != null && dto.getFacilityBookingId() != null) {
			throw new IllegalArgumentException("Provide either bookingId or facilityBookingId, not both.");
		}

		SupportTicket entity = toEntity(dto);
		Integer generatedId = supportTicketRepository.addTicket(entity);
		entity.setTicketId(generatedId);

		return toDto(entity);
	}

	@Override
	public Optional<SupportTicketDto> getTicketById(Integer ticketId) {
		return supportTicketRepository.getTicketById(ticketId).map(this::toDto);
	}

	@Override
	public List<SupportTicketDto> getAllTickets() {
		return supportTicketRepository.getAllTickets().stream().map(this::toDto).collect(Collectors.toList());
	}

	@Override
	public List<SupportTicketDto> getTicketsByCustomer(Integer customerId) {
		return supportTicketRepository.getTicketsByCustomer(customerId).stream().map(this::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupportTicketDto> getTicketsByStatus(String status) {
		return supportTicketRepository.getTicketsByStatus(status).stream().map(this::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupportTicketDto> getTicketsByAssignedStaff(Integer staffId) {
		return supportTicketRepository.getTicketsByAssignedStaff(staffId).stream().map(this::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public SupportTicketDto updateTicket(Integer ticketId, SupportTicketDto dto) {
		SupportTicket existing = supportTicketRepository.getTicketById(ticketId)
				.orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketId));

		// Only allow certain fields to be updated (not ticketId or createdAt)
		existing.setSubject(dto.getSubject());
		existing.setCategory(dto.getCategory());
		existing.setDetails(dto.getDetails());
		existing.setAssignedStaffId(dto.getAssignedStaffId());
		existing.setBookingId(dto.getBookingId());
		existing.setFacilityBookingId(dto.getFacilityBookingId());

		supportTicketRepository.updateTicket(ticketId, existing);

		// Return updated view
		return supportTicketRepository.getTicketById(ticketId).map(this::toDto).orElse(null);
	}

	@Override
	public void changeStatus(Integer ticketId, String status) {
		SupportTicket existing = supportTicketRepository.getTicketById(ticketId)
				.orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketId));

		// optionally validate status values
		if (!("OPEN".equals(status) || "IN_PROGRESS".equals(status) || "CLOSED".equals(status))) {
			throw new IllegalArgumentException("Invalid status. Allowed: OPEN, IN_PROGRESS, CLOSED");
		}

		supportTicketRepository.changeStatus(ticketId, status);
	}

	@Override
	public void assignStaff(Integer ticketId, Integer staffId) {
		SupportTicket existing = supportTicketRepository.getTicketById(ticketId)
				.orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketId));

		supportTicketRepository.assignStaff(ticketId, staffId);
	}
}