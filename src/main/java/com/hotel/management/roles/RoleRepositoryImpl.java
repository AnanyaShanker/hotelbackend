package com.hotel.management.roles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

	@Autowired
	private JdbcTemplate jdbc;

	private final RowMapper<Role> rowMapper = (rs, rowNum) -> {
		Role role = new Role();
		role.setRoleId(rs.getInt("role_id"));
		role.setRoleName(rs.getString("role_name"));
		role.setDescription(rs.getString("description"));
		return role;
	};

	@Override
	public int save(Role role) {
		String sql = "INSERT INTO roles (role_name, description) VALUES (?, ?)";
		jdbc.update(sql, role.getRoleName(), role.getDescription());
		return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
	}

	@Override
	public List<Role> findAll() {
		String sql = "SELECT * FROM roles";
		return jdbc.query(sql, rowMapper);
	}

	@Override
	public Optional<Role> findById(int id) {
		String sql = "SELECT * FROM roles WHERE role_id = ?";
		List<Role> list = jdbc.query(sql, rowMapper, id);
		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public int update(Role role) {
		String sql = "UPDATE roles SET role_name = ?, description = ? WHERE role_id = ?";
		return jdbc.update(sql, role.getRoleName(), role.getDescription(), role.getRoleId());
	}

	@Override
	public int delete(int id) {
		String sql = "DELETE FROM roles WHERE role_id = ?";
		return jdbc.update(sql, id);
	}
}