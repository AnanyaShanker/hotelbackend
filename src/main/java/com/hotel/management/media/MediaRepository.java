package com.hotel.management.media;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MediaRepository {

    private final JdbcTemplate jdbcTemplate;

    public MediaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------- SAVE --------------------
    public int save(MediaUploadRequest req) {
        String sql = "INSERT INTO media(owner_type, owner_id, file_name, file_type, file_size, file_path, uploaded_by) "
                   + "VALUES(?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"media_id"});
            ps.setString(1, req.getOwnerType());
            ps.setObject(2, req.getOwnerId());
            ps.setString(3, req.getFileName());
            ps.setString(4, req.getFileType());
            ps.setObject(5, req.getFileSize());
            ps.setString(6, req.getFilePath());
            ps.setObject(7, req.getUploadedBy());
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
    }

    // -------------------- FIND BY ID --------------------
    public Media findById(int id) {
        String sql = "SELECT * FROM media WHERE media_id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapToMedia(rs), id);
    }

    // -------------------- FIND ALL --------------------
    public List<Media> findAll() {
        String sql = "SELECT * FROM media";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapToMedia(rs));
    }

    // -------------------- DELETE --------------------
    public boolean delete(int id) {
        String sql = "DELETE FROM media WHERE media_id=?";
        return jdbcTemplate.update(sql, id) > 0;
    }
    
    
 // find by owner type + owner id
    public List<com.hotel.management.media.Media> findByOwner(String ownerType, Integer ownerId) {
        String sql = "SELECT * FROM media WHERE owner_type=? AND owner_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapToMedia(rs), ownerType, ownerId);
    }


    // -------------------- MAPPER --------------------
    private Media mapToMedia(ResultSet rs) throws SQLException {
        Media m = new Media();
        m.setMediaId(rs.getInt("media_id"));
        m.setOwnerType(rs.getString("owner_type"));
        m.setOwnerId((Integer) rs.getObject("owner_id"));
        m.setFileName(rs.getString("file_name"));
        m.setFileType(rs.getString("file_type"));
        m.setFileSize((Long) rs.getObject("file_size"));
        m.setFilePath(rs.getString("file_path"));
        m.setFileBlob(rs.getBytes("file_blob")); // may be null
        m.setUploadedBy((Integer) rs.getObject("uploaded_by"));
        m.setUploadedAt(rs.getTimestamp("uploaded_at").toString());
        return m;
    }
}
