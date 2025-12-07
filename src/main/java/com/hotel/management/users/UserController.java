
package com.hotel.management.users;
 
import java.util.List;

import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PatchMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
 
import com.hotel.management.dto.ApiResponseDTO;

import com.hotel.management.auth.JwtUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")

public class UserController {
 
	@Autowired

	private UserService userService;
 
	@Autowired

	private JwtUtil jwtUtil;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

	public ResponseEntity<ApiResponseDTO<UserDTO>> createUser(@ModelAttribute UserDTO dto) {

		UserDTO created = userService.createUser(dto);

		ApiResponseDTO<UserDTO> resp = new ApiResponseDTO<>(201, "User created successfully", created);

		return ResponseEntity.status(201).body(resp);

	}
 
	@PostMapping("/login")
	public ResponseEntity<ApiResponseDTO<?>> login(@RequestBody UserDTO dto) {
		// Validate credentials and get user info
		UserDTO user = userService.login(dto.getEmail(), dto.getPassword());

		int userId = user.getUserId();
		int roleId = user.getRoleId();

		// Generate JWT token
		String token = jwtUtil.generateToken(userId, roleId);

		// Build response with user data
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("user", user);
		responseData.put("token", token);
		responseData.put("userId", userId);
		responseData.put("roleId", roleId);
		responseData.put("name", user.getName());
		responseData.put("email", user.getEmail());

		// ✅ If Manager (role_id = 3), find their branch
		if (roleId == 3) {
			try {
				String sql = "SELECT branch_id, name FROM hotel_branches WHERE manager_id = ?";
				Map<String, Object> branchInfo = jdbcTemplate.queryForMap(sql, userId);
				responseData.put("branchId", branchInfo.get("branch_id"));
				responseData.put("branchName", branchInfo.get("name"));
			} catch (EmptyResultDataAccessException e) {
				// Manager not assigned to any branch yet
				responseData.put("branchId", null);
				responseData.put("branchName", null);
			}
		}

		// ✅ If Staff (role_id = 2), find their staff_id and branch
		if (roleId == 2) {
			try {
				String sql = "SELECT s.staff_id, s.hotel_id, hb.name as branch_name " +
							 "FROM staff s " +
							 "LEFT JOIN hotel_branches hb ON s.hotel_id = hb.branch_id " +
							 "WHERE s.user_id = ?";
				Map<String, Object> staffInfo = jdbcTemplate.queryForMap(sql, userId);
				responseData.put("staffId", staffInfo.get("staff_id"));
				responseData.put("branchId", staffInfo.get("hotel_id"));
				responseData.put("branchName", staffInfo.get("branch_name"));
			} catch (EmptyResultDataAccessException e) {
				// Staff not created yet
				responseData.put("staffId", null);
				responseData.put("branchId", null);
				responseData.put("branchName", null);
			}
		}

		return ResponseEntity.ok(
			new ApiResponseDTO<>(200, "Login successful", responseData)
		);
	}
 
	@GetMapping

	public ResponseEntity<ApiResponseDTO<List<UserDTO>>> getAllUsers() {

		List<UserDTO> list = userService.getAllUsers();

		ApiResponseDTO<List<UserDTO>> resp = new ApiResponseDTO<>(200, "Users fetched successfully", list);

		return ResponseEntity.ok(resp);

	}
 
	

	@GetMapping("/{id}")

	public ResponseEntity<ApiResponseDTO<UserDTO>> getUserById(@PathVariable int id) {

		UserDTO u = userService.getUserById(id);

		ApiResponseDTO<UserDTO> resp = new ApiResponseDTO<>(200, "User fetched successfully", u);

		return ResponseEntity.ok(resp);

	}
 
	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

	public ResponseEntity<ApiResponseDTO<UserDTO>> updateUser(@PathVariable int id, @ModelAttribute UserDTO dto) {

		UserDTO updated = userService.updateUser(id, dto);

		ApiResponseDTO<UserDTO> resp = new ApiResponseDTO<>(200, "User updated successfully", updated);

		return ResponseEntity.ok(resp);

	}
 
	@PatchMapping("/{id}/toggle-status")

	public ResponseEntity<ApiResponseDTO<Void>> toggleStatus(@PathVariable int id) {

		int result = userService.toggleStatus(id);

		ApiResponseDTO<Void> resp = new ApiResponseDTO<>(200,

				"User " + (result == 1 ? "activated" : "deactivated") + " successfully", null);

		return ResponseEntity.ok(resp);

	}
 
	@DeleteMapping("/{id}")

	public ResponseEntity<ApiResponseDTO<Void>> deleteUser(@PathVariable int id) {

		userService.deleteUser(id);

		ApiResponseDTO<Void> resp = new ApiResponseDTO<>(200, "User deleted successfully", null);

		return ResponseEntity.ok(resp);

	}

}
 