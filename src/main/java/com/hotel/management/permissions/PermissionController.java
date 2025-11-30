package com.hotel.management.permissions;

import com.hotel.management.dto.ApiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

	private final PermissionService permissionService;

	public PermissionController(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	@PostMapping
	public ResponseEntity<PermissionDTO> create(@RequestBody PermissionDTO dto) {
		return ResponseEntity.status(201).body(permissionService.create(dto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PermissionDTO> getById(@PathVariable int id) {
		return ResponseEntity.ok(permissionService.getById(id));
	}

	@GetMapping
	public ResponseEntity<List<PermissionDTO>> getAll() {
		return ResponseEntity.ok(permissionService.getAll());
	}

	@PutMapping("/{id}")
	public ResponseEntity<PermissionDTO> update(@PathVariable int id, @RequestBody PermissionDTO dto) {
		return ResponseEntity.ok(permissionService.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable int id) {
		permissionService.delete(id);
		return ResponseEntity.ok("Deleted successfully");
	}
}
