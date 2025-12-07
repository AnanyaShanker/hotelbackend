package com.hotel.management.branches;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

	private final BranchService branchService;

	public BranchController(BranchService branchService) {
		this.branchService = branchService;
	}

	@PostMapping
	public ResponseEntity<BranchResponseDTO> addBranch(@Valid @RequestBody BranchRequestDTO request) {

		BranchResponseDTO dto = branchService.addBranch(request);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BranchResponseDTO> getBranch(@PathVariable Integer id) {

		BranchResponseDTO dto = branchService.getBranchById(id);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/manager/{managerId}")
	public ResponseEntity<BranchResponseDTO> getBranchByManager(@PathVariable Integer managerId) {
	    BranchResponseDTO dto = branchService.getBranchByManager(managerId);
	    return ResponseEntity.ok(dto);
	}

	@GetMapping
	public ResponseEntity<List<BranchResponseDTO>> getAllBranches() {

		List<BranchResponseDTO> list = branchService.getAllBranches();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/search")
	public ResponseEntity<List<BranchResponseDTO>> getBranchesByLocation(@RequestParam String location) {

		List<BranchResponseDTO> list = branchService.getBranchesByLocation(location);
		return ResponseEntity.ok(list);
	}

	@PutMapping("/{id}")
	public ResponseEntity<BranchResponseDTO> updateBranch(@PathVariable Integer id,
			@Valid @RequestBody BranchRequestDTO request) {

		BranchResponseDTO dto = branchService.updateBranch(id, request);
		return ResponseEntity.ok(dto);
	}

	@PatchMapping("/{id}/toggle-status")
	public ResponseEntity<String> toggleStatus(@PathVariable Integer id) {

		branchService.toggleStatus(id);
		return ResponseEntity.ok("Branch status toggled successfully.");
	}
}