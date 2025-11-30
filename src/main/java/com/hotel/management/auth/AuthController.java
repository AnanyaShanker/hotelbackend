package com.hotel.management.auth;
 
import com.hotel.management.dto.ApiResponseDTO;
import com.hotel.management.branches.ResourceNotFoundException;
import com.hotel.management.users.UserDTO;
import com.hotel.management.users.UserService;
 
import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
// DTOs for request/response below
@RestController
@RequestMapping("/api/auth")
public class AuthController {
 
	@Autowired
	private UserService userService; // uses your existing login(email,password)
 
	@Autowired
	private JwtUtil jwtUtils;
 
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
		// Use your existing login method to validate credentials and get user info
		UserDTO userDto = userService.login(req.getEmail(), req.getPassword()); // throws on invalid
 
		int userId = userDto.getUserId();
		int roleId = userDto.getRoleId();
 
		String token = jwtUtils.generateToken(userId, roleId);
		LoginResponse resp = new LoginResponse(token, userId, roleId);
		return ResponseEntity.ok(resp);
	}
	// ---------------- FORGOT PASSWORD STEP 2 (RESET) ----------------
	@PostMapping("/reset-password")
	public ResponseEntity<ApiResponseDTO<?>> resetPassword(@RequestBody Map<String, String> req) {
 
	    String email = req.get("email");
	    String securityAnswer = req.get("securityAnswer");
	    String newPassword = req.get("newPassword");
 
	    boolean success = userService.resetPassword(email, securityAnswer, newPassword);
 
	    if (!success) {
	        // WRONG ANSWER → do NOT say success
	        return ResponseEntity.status(400)
	                .body(new ApiResponseDTO<>(
	                        400,
	                        "Incorrect security answer. Password was NOT reset.",
	                        null
	                ));
	    }
 
	    // ONLY HERE if answer correct
	    return ResponseEntity.ok(
	            new ApiResponseDTO<>(
	                    200,
	                    "Password reset successfully.",
	                    null
	            )
	    );
	}
 
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> forgotPassword(@RequestBody Map<String, String> request) {
     String email = request.get("email");
 
     try {
     Map<String, Object> question = userService.getSecurityQuestion(email);
     return ResponseEntity.ok(
     new ApiResponseDTO<>(200, "Security question fetched successfully", question)
     );
     } catch (ResourceNotFoundException ex) {
     return ResponseEntity.status(404).body(
     new ApiResponseDTO<>(404, ex.getMessage(), null)
     );
     }
    }
 
 
}
 
class LoginRequest {
	private String email;
	private String password;
 
	// getters/setters
	public String getEmail() {
		return email;
	}
 
	public void setEmail(String e) {
		this.email = e;
	}
 
	public String getPassword() {
		return password;
	}
 
	public void setPassword(String p) {
		this.password = p;
	}
}
 
class LoginResponse {
	private String token;
	private int userId;
	private int roleId;
 
	public LoginResponse(String token, int userId, int roleId) {
		this.token = token;
		this.userId = userId;
		this.roleId = roleId;
	}
 
 
	// getters
	public String getToken() {
		return token;
	}
 
	public int getUserId() {
		return userId;
	}
 
	public int getRoleId() {
		return roleId;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}