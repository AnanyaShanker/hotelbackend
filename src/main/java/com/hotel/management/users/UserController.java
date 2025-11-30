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
 
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")

public class UserController {
 
	@Autowired

	private UserService userService;
 
	@Autowired

	private JwtUtil jwtUtil;
 
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

	public ResponseEntity<ApiResponseDTO<UserDTO>> createUser(@ModelAttribute UserDTO dto) {

		UserDTO created = userService.createUser(dto);

		ApiResponseDTO<UserDTO> resp = new ApiResponseDTO<>(201, "User created successfully", created);

		return ResponseEntity.status(201).body(resp);

	}
 
	@PostMapping("/login")

	public ResponseEntity<ApiResponseDTO<?>> login(@RequestBody UserDTO dto) {

	 UserDTO user = userService.login(dto.getEmail(), dto.getPassword());
 
	 String token = jwtUtil.generateToken(user.getUserId(), user.getRoleId());
 
	 return ResponseEntity.ok(

	 new ApiResponseDTO<>(200,

	 "Login successful",

	 Map.of(

	 "user", user,

	 "token", token

	 )

	 )

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
 