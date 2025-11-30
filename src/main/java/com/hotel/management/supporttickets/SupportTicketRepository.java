package com.hotel.management.supporttickets;

import java.util.List;
import java.util.Optional;

public interface SupportTicketRepository {

	Integer addTicket(SupportTicket ticket);

	Optional<SupportTicket> getTicketById(Integer ticketId);

	List<SupportTicket> getAllTickets();

	List<SupportTicket> getTicketsByCustomer(Integer customerId);

	List<SupportTicket> getTicketsByStatus(String status);

	List<SupportTicket> getTicketsByAssignedStaff(Integer staffId);

	void updateTicket(Integer ticketId, SupportTicket ticket);

	void changeStatus(Integer ticketId, String status);

	void assignStaff(Integer ticketId, Integer staffId);

//	void deleteTicket(Integer ticketId); 
}