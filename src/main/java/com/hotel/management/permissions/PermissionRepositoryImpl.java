package com.hotel.management.permissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PermissionRepositoryImpl implements PermissionRepository {

	private final JdbcTemplate jdbcTemplate;

	public PermissionRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int save(Permission permission) {
		final String sql = "INSERT INTO permissions (permission_name, description) VALUES (?, ?)";
		jdbcTemplate.update(sql, permission.getPermissionName().name(), permission.getDescription());
		// returns last insert id
		return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
	}

	@Override
	public Optional<Permission> findById(int id) {
		final String sql = "SELECT permission_id, permission_name, description FROM permissions WHERE permission_id = ?";
		List<Permission> list = jdbcTemplate.query(sql, new Object[] { id }, new PermissionRowMapper());
		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public List<Permission> findAll() {
		final String sql = "SELECT permission_id, permission_name, description FROM permissions";
		return jdbcTemplate.query(sql, new PermissionRowMapper());
	}

	@Override
	public void update(Permission permission) {
		final String sql = "UPDATE permissions SET permission_name = ?, description = ? WHERE permission_id = ?";
		jdbcTemplate.update(sql, permission.getPermissionName().name(), permission.getDescription(),
				permission.getPermissionId());
	}

	@Override
	public void delete(int id) {
		final String sql = "DELETE FROM permissions WHERE permission_id = ?";
		jdbcTemplate.update(sql, id);
	}

	private static class PermissionRowMapper implements org.springframework.jdbc.core.RowMapper<Permission> {
		@Override
		public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
			Permission p = new Permission();
			p.setPermissionId(rs.getInt("permission_id"));
			p.setPermissionName(PermissionName.valueOf(rs.getString("permission_name")));
			p.setDescription(rs.getString("description"));
			return p;
		}
	}
}