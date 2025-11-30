package com.hotel.management.supporttickets;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
public class SupportTicketController {

	@Autowired
	private SupportTicketService supportService;

	@PostMapping
	public ResponseEntity<SupportTicketDto> createTicket(@Valid @RequestBody SupportTicketDto dto) {
		SupportTicketDto created = supportService.createTicket(dto);
		return ResponseEntity.ok(created);
	}

	@GetMapping
	public ResponseEntity<List<SupportTicketDto>> getAllTickets() {
		return ResponseEntity.ok(supportService.getAllTickets());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SupportTicketDto> getTicket(@PathVariable Integer id) {
		return supportService.getTicketById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<SupportTicketDto>> getByCustomer(@PathVariable Integer customerId) {
		return ResponseEntity.ok(supportService.getTicketsByCustomer(customerId));
	}

	@GetMapping("/status")
	public ResponseEntity<List<SupportTicketDto>> getByStatus(@RequestParam String status) {
		return ResponseEntity.ok(supportService.getTicketsByStatus(status));
	}

	@GetMapping("/assigned/{staffId}")
	public ResponseEntity<List<SupportTicketDto>> getByAssigned(@PathVariable Integer staffId) {
		return ResponseEntity.ok(supportService.getTicketsByAssignedStaff(staffId));
	}

	@PutMapping("/{id}")
	public ResponseEntity<SupportTicketDto> updateTicket(@PathVariable Integer id,
			@Valid @RequestBody SupportTicketDto dto) {
		SupportTicketDto updated = supportService.updateTicket(id, dto);
		return ResponseEntity.ok(updated);
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<String> changeStatus(@PathVariable Integer id, @RequestParam String status) {
		supportService.changeStatus(id, status);
		return ResponseEntity.ok("Status changed successfully.");
	}

	@PatchMapping("/{id}/assign")
	public ResponseEntity<String> assignStaff(@PathVariable Integer id, @RequestParam Integer staffId) {
		supportService.assignStaff(id, staffId);
		return ResponseEntity.ok("Staff assigned successfully.");
	}
}