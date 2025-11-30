package com.hotel.management.notifications;

import com.hotel.management.notifications.NotificationResponseDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class NotificationRepository {

    private final JdbcTemplate jdbc;

    public NotificationRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public Integer save(Integer userId, String notificationType, String message, String sentVia) {
        final String sql = "INSERT INTO notifications (user_id, notification_type, message, sent_via, status, created_at, is_deleted) " +
                "VALUES (?, ?, ?, ?, 'PENDING', NOW(), 0)";

        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setString(2, notificationType);
            ps.setString(3, message);
            ps.setString(4, sentVia);
            return ps;
        }, kh);

        Number key = Objects.requireNonNull(kh.getKey());
        return key.intValue();
    }

    public NotificationResponseDTO findById(Integer id) {
        final String sql =
                "SELECT n.*, u.name AS user_name, u.email AS user_email " +
                "FROM notifications n JOIN users u ON n.user_id = u.user_id " +
                "WHERE n.notification_id = ? AND n.is_deleted = 0";

        List<NotificationResponseDTO> list = jdbc.query(sql, (rs, rowNum) -> {
            NotificationResponseDTO dto = new NotificationResponseDTO();
            dto.setNotificationId(rs.getInt("notification_id"));
            dto.setUserId(rs.getInt("user_id"));
            dto.setNotificationType(rs.getString("notification_type"));
            dto.setMessage(rs.getString("message"));
            dto.setSentVia(rs.getString("sent_via"));
            dto.setStatus(rs.getString("status"));
            dto.setCreatedAt(rs.getString("created_at"));
            dto.setIsDeleted(rs.getInt("is_deleted") == 1);
            dto.setDeletedAt(rs.getString("deleted_at"));
            dto.setUserName(rs.getString("user_name"));
            dto.setUserEmail(rs.getString("user_email"));
            return dto;
        }, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<NotificationResponseDTO> findByUserId(Integer userId) {
        final String sql =
                "SELECT n.*, u.name AS user_name, u.email AS user_email " +
                "FROM notifications n JOIN users u ON n.user_id = u.user_id " +
                "WHERE n.user_id = ? AND n.is_deleted = 0 ORDER BY n.created_at DESC";

        return jdbc.query(sql, (rs, rowNum) -> {
            NotificationResponseDTO dto = new NotificationResponseDTO();
            dto.setNotificationId(rs.getInt("notification_id"));
            dto.setUserId(rs.getInt("user_id"));
            dto.setNotificationType(rs.getString("notification_type"));
            dto.setMessage(rs.getString("message"));
            dto.setSentVia(rs.getString("sent_via"));
            dto.setStatus(rs.getString("status"));
            dto.setCreatedAt(rs.getString("created_at"));
            dto.setIsDeleted(rs.getInt("is_deleted") == 1);
            dto.setDeletedAt(rs.getString("deleted_at"));
            dto.setUserName(rs.getString("user_name"));
            dto.setUserEmail(rs.getString("user_email"));
            return dto;
        }, userId);
    }

    public boolean updateStatus(Integer id, String newStatus) {
        final String sql = "UPDATE notifications SET status = ? WHERE notification_id = ? AND is_deleted = 0";
        int rows = jdbc.update(sql, newStatus, id);
        return rows > 0;
    }

    public boolean softDelete(Integer id) {
        final String sql = "UPDATE notifications SET is_deleted = 1, deleted_at = NOW() WHERE notification_id = ? AND is_deleted = 0";
        int rows = jdbc.update(sql, id);
        return rows > 0;
    }
}
