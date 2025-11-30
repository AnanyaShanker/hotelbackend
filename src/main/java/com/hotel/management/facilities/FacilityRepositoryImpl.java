package com.hotel.management.facilities;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FacilityRepositoryImpl implements FacilityRepository {

    private final JdbcTemplate jdbcTemplate;

    public FacilityRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Facility facility) {
        String sql = "INSERT INTO facilities(name, type, price, capacity, event_start, event_end, status, " +
                "facility_primary_image, brochure_document, description, location) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        return jdbcTemplate.update(sql,
                facility.getName(),
                facility.getType(),
                facility.getPrice(),
                facility.getCapacity(),
                facility.getEventStart(),
                facility.getEventEnd(),
                facility.getStatus(),
                facility.getFacilityPrimaryImage(),
                facility.getBrochureDocument(),
                facility.getDescription(),
                facility.getLocation()
        );
    }

    @Override
    public Facility findById(int id) {
        String sql = "SELECT * FROM facilities WHERE facility_id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRow(rs), id);
    }

    @Override
    public List<Facility> findAll() {
        String sql = "SELECT * FROM facilities";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public boolean update(Facility facility) {
        String sql = "UPDATE facilities SET name=?, type=?, price=?, capacity=?, event_start=?, event_end=?, " +
                "status=?, facility_primary_image=?, brochure_document=?, description=?, location=? " +
                "WHERE facility_id=?";

        return jdbcTemplate.update(sql,
                facility.getName(),
                facility.getType(),
                facility.getPrice(),
                facility.getCapacity(),
                facility.getEventStart(),
                facility.getEventEnd(),
                facility.getStatus(),
                facility.getFacilityPrimaryImage(),
                facility.getBrochureDocument(),
                facility.getDescription(),
                facility.getLocation(),
                facility.getFacilityId()
        ) > 0;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM facilities WHERE facility_id=?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public boolean updatePrimaryImage(int facilityId, String path) {
        String sql = "UPDATE facilities SET facility_primary_image=?, updated_at=CURRENT_TIMESTAMP WHERE facility_id=?";
        return jdbcTemplate.update(sql, path, facilityId) > 0;
    }

    @Override
    public boolean updateBrochure(int facilityId, String path) {
        String sql = "UPDATE facilities SET brochure_document=?, updated_at=CURRENT_TIMESTAMP WHERE facility_id=?";
        return jdbcTemplate.update(sql, path, facilityId) > 0;
    }

    private Facility mapRow(ResultSet rs) throws SQLException {
        Facility f = new Facility();

        f.setFacilityId(rs.getInt("facility_id"));
        f.setName(rs.getString("name"));
        f.setType(rs.getString("type"));
        f.setPrice(rs.getDouble("price"));
        f.setCapacity(rs.getInt("capacity"));

        if (rs.getTimestamp("event_start") != null)
            f.setEventStart(rs.getTimestamp("event_start").toString());

        if (rs.getTimestamp("event_end") != null)
            f.setEventEnd(rs.getTimestamp("event_end").toString());

        f.setStatus(rs.getString("status"));
        f.setFacilityPrimaryImage(rs.getString("facility_primary_image"));
        f.setBrochureDocument(rs.getString("brochure_document"));
        f.setDescription(rs.getString("description"));
        f.setLocation(rs.getString("location"));

        if (rs.getTimestamp("created_at") != null)
            f.setCreatedAt(rs.getTimestamp("created_at").toString());

        if (rs.getTimestamp("updated_at") != null)
            f.setUpdatedAt(rs.getTimestamp("updated_at").toString());

        return f;
    }
}
