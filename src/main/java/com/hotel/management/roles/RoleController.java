package com.hotel.management.roles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.management.dto.ApiResponseDTO;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@PostMapping
	public ResponseEntity<ApiResponseDTO<RoleDTO>> create(@RequestBody RoleDTO dto) {
		RoleDTO created = roleService.createRole(dto);
		ApiResponseDTO<RoleDTO> response = new ApiResponseDTO<>(201, "Role created successfully", created);

		return ResponseEntity.status(201).body(response);
	}

	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<RoleDTO>>> getAll() {
		List<RoleDTO> roles = roleService.getAllRoles();
		ApiResponseDTO<List<RoleDTO>> response = new ApiResponseDTO<>(200, "Roles fetched successfully", roles);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<RoleDTO>> getById(@PathVariable int id) {
		RoleDTO role = roleService.getRoleById(id);
		ApiResponseDTO<RoleDTO> response = new ApiResponseDTO<>(200, "Role fetched successfully", role);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<RoleDTO>> update(@PathVariable int id, @RequestBody RoleDTO dto) {

		RoleDTO updated = roleService.updateRole(id, dto);
		ApiResponseDTO<RoleDTO> response = new ApiResponseDTO<>(200, "Role updated successfully", updated);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable int id) {
		roleService.deleteRole(id);
		ApiResponseDTO<Void> response = new ApiResponseDTO<>(200, "Role deleted successfully", null);

		return ResponseEntity.ok(response);
	}
}
