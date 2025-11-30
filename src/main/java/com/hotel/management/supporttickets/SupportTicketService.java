package com.hotel.management.supporttickets;

import java.util.List;
import java.util.Optional;

public interface SupportTicketService {

	SupportTicketDto createTicket(SupportTicketDto dto);

	Optional<SupportTicketDto> getTicketById(Integer ticketId);

	List<SupportTicketDto> getAllTickets();

	List<SupportTicketDto> getTicketsByCustomer(Integer customerId);

	List<SupportTicketDto> getTicketsByStatus(String status);

	List<SupportTicketDto> getTicketsByAssignedStaff(Integer staffId);

	SupportTicketDto updateTicket(Integer ticketId, SupportTicketDto dto);

	void changeStatus(Integer ticketId, String status);

	void assignStaff(Integer ticketId, Integer staffId);
}