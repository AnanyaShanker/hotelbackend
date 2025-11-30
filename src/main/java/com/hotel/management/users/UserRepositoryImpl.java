package com.hotel.management.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setRoleId(rs.getInt("role_id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setPasswordSalt(rs.getString("password_salt"));
        u.setPhone(rs.getString("phone"));
        u.setProfileImage(rs.getString("profile_image"));
        u.setiDocument(rs.getString("id_document"));
        u.setNotes(rs.getString("notes"));
        u.setStatus(rs.getString("status"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        u.setUpdatedAt(rs.getTimestamp("updated_at"));
        // Security fields
        u.setSecurityQuestions(rs.getString("securityQuestion"));
        u.setSecurityAnswerHash(rs.getString("securityAnswerHash"));
        u.setSecurityAnswerSalt(rs.getString("securityAnswerSalt"));
        return u;
    };

    @Override
    public int save(User user) {
        String sql = "INSERT INTO users (role_id, name, email, password_hash, password_salt, phone, profile_image, id_document, notes, status, securityQuestion, securityAnswerHash, securityAnswerSalt, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        jdbcTemplate.update(sql, user.getRoleId(), user.getName(), user.getEmail(), user.getPasswordHash(),
                user.getPasswordSalt(), user.getPhone(), user.getProfileImage(), user.getiDocument(), user.getNotes(),
                user.getStatus() == null ? "active" : user.getStatus(),
                user.getSecurityQuestions(), user.getSecurityAnswerHash(), user.getSecurityAnswerSalt());

        Integer generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        return generatedId == null ? 0 : generatedId;
    }

    @Override
    public Optional<User> findById(int userId) {
        try {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            User u = jdbcTemplate.queryForObject(sql, rowMapper, userId);
            return Optional.ofNullable(u);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            String sql = "SELECT * FROM users WHERE email = ?";
            User u = jdbcTemplate.queryForObject(sql, rowMapper, email);
            return Optional.ofNullable(u);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public int update(User user) {
        String sql = "UPDATE users SET role_id = ?, name = ?, phone = ?, profile_image = ?, id_document = ?, notes = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        return jdbcTemplate.update(sql, user.getRoleId(), user.getName(), user.getPhone(), user.getProfileImage(),
                user.getiDocument(), user.getNotes(), user.getStatus(), user.getUserId());
    }

    @Override
    public int updatePassword(int userId, String newHash) {
        String sql = "UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        return jdbcTemplate.update(sql, newHash, userId);
    }

    @Override
    public int updatePasswordAndSalt(int userId, String newSalt, String newHash) {
        String sql = "UPDATE users SET password_salt = ?, password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        return jdbcTemplate.update(sql, newSalt, newHash, userId);
    }

    @Override
    public int updateStatus(int userId, String newStatus) {
        String sql = "UPDATE users SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        return jdbcTemplate.update(sql, newStatus, userId);
    }

    @Override
    public int deleteById(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }

    @Override
    public String getEmailById(int id) {
        try {
            String sql = "SELECT email FROM users WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, String.class, id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public boolean existsByIdAndActive(int id) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ? AND status = 'active'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
