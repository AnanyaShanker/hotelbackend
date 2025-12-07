    package com.hotel.management.auth;

    import com.hotel.management.dto.ApiResponseDTO;
    import com.hotel.management.branches.ResourceNotFoundException;
    import com.hotel.management.users.UserDTO;
    import com.hotel.management.users.UserService;

    import java.util.Map;
    import java.util.HashMap;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.dao.EmptyResultDataAccessException;

    
    @RestController
    @RequestMapping("/api/auth")
    public class AuthController {

        @Autowired
        private UserService userService; 

        @Autowired
        private JwtUtil jwtUtils;

        @Autowired
        private JdbcTemplate jdbcTemplate;

        /**
         * ✅ ENHANCED: Login with automatic branch detection
         * POST /api/auth/login
         * Returns standardized ApiResponseDTO format
         */
        @PostMapping("/login")
        public ResponseEntity<ApiResponseDTO<Map<String, Object>>> login(@RequestBody LoginRequest req) {
            try {
               
                UserDTO userDto = userService.login(req.getEmail(), req.getPassword()); 

                int userId = userDto.getUserId();
                int roleId = userDto.getRoleId();

                String token = jwtUtils.generateToken(userId, roleId);

                
                Map<String, Object> user = new HashMap<>();
                user.put("userId", userId);
                user.put("roleId", roleId);
                user.put("name", userDto.getName());
                user.put("email", userDto.getEmail());

                
                if (roleId == 3) {
                    try {
                        String sql = "SELECT branch_id, name FROM hotel_branches WHERE manager_id = ?";
                        Map<String, Object> branchInfo = jdbcTemplate.queryForMap(sql, userId);
                        user.put("branchId", branchInfo.get("branch_id"));
                        user.put("branchName", branchInfo.get("name"));
                    } catch (EmptyResultDataAccessException e) {
                        
                        user.put("branchId", null);
                        user.put("branchName", null);
                    }
                }

                
                if (roleId == 2) {
                    try {
                        String sql = "SELECT s.staff_id, s.hotel_id, hb.name as branch_name " +
                                     "FROM staff s " +
                                     "LEFT JOIN hotel_branches hb ON s.hotel_id = hb.branch_id " +
                                     "WHERE s.user_id = ?";
                        Map<String, Object> staffInfo = jdbcTemplate.queryForMap(sql, userId);
                        user.put("staffId", staffInfo.get("staff_id"));
                        user.put("branchId", staffInfo.get("hotel_id"));
                        user.put("branchName", staffInfo.get("branch_name"));
                    } catch (EmptyResultDataAccessException e) {
                        
                        user.put("staffId", null);
                        user.put("branchId", null);
                        user.put("branchName", null);
                    }
                }

                
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("token", token);
                responseData.put("user", user);

                
                ApiResponseDTO<Map<String, Object>> response = new ApiResponseDTO<>(
                    200,
                    "Login successful",
                    responseData
                );

                return ResponseEntity.ok(response);

            } catch (RuntimeException e) {
                
                ApiResponseDTO<Map<String, Object>> errorResponse = new ApiResponseDTO<>(
                    401,
                    e.getMessage() != null ? e.getMessage() : "Invalid email or password",
                    null
                );
                return ResponseEntity.status(401).body(errorResponse);
            }
        }

        
        @PostMapping("/reset-password")
        public ResponseEntity<ApiResponseDTO<?>> resetPassword(@RequestBody Map<String, String> req) {

            String email = req.get("email");
            String securityAnswer = req.get("securityAnswer");
            String newPassword = req.get("newPassword");

            boolean success = userService.resetPassword(email, securityAnswer, newPassword);

            if (!success) {
                
                return ResponseEntity.status(400)
                        .body(new ApiResponseDTO<>(
                                400,
                                "Incorrect security answer. Password was NOT reset.",
                                null
                        ));
            }

           
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




