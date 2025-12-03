package com.hotel.management.users;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    // Core CRUD operations
    int save(User user);
    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    int update(User user);
    int deleteById(int id);

    // Password & security updates
    int updatePassword(int userId, String newHash);
    int updatePasswordAndSalt(int userId, String newSalt, String newHash);

    // Status updates
    int updateStatus(int userId, String newStatus);

    // Utility queries
    boolean existsByIdAndActive(int id);
    String getEmailById(int id);
    boolean emailExists(String email);
}
