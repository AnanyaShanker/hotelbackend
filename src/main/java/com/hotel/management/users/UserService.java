package com.hotel.management.users;

import java.util.List;
import java.util.Map;

public interface UserService {

    // Core user operations
    UserDTO createUser(UserDTO dto);
    UserDTO getUserById(int userId);
    UserDTO getUserByEmail(String email);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(int id, UserDTO dto);
    void deleteUser(int userId);

    // Authentication
    UserDTO login(String email, String password);

    // Status management
    int toggleStatus(int userId);

    // Security & password recovery
    Map<String, Object> getSecurityQuestion(String email);
    boolean resetPassword(String email, String answer, String newPassword);
}
