package com.hotel.management.branches;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;


import java.util.*;

@Repository
public class BranchRepositoryImpl implements BranchRepository {

	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insert;

	private final RowMapper<Branch> mapper = new BranchRowMapper();

	public BranchRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;

		this.insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("hotel_branches")
				.usingGeneratedKeyColumns("branch_id");
	}
	
	@Override
	public Optional<Branch> findByManagerId(Integer managerId) {
	    try {
	        Branch branch = jdbcTemplate.queryForObject(
	            "SELECT * FROM hotel_branches WHERE manager_id = ?",
	            mapper,
	            managerId
	        );
	        return Optional.ofNullable(branch);
	    } catch (EmptyResultDataAccessException ex) {
	        return Optional.empty();
	    }
	}

	
	@Override
	public Integer addBranch(Branch branch) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", branch.getName());
		params.put("location", branch.getLocation());
		params.put("contact_number", branch.getContactNumber());
		params.put("manager_id", branch.getManagerId());
		params.put("total_rooms", branch.getTotalRooms());
		params.put("status", branch.getStatus());

		Number key = insert.executeAndReturnKey(params);
		return key.intValue();
	}

	
	@Override
	public Optional<Branch> findById(Integer id) {
		try {
			Branch branch = jdbcTemplate.queryForObject("SELECT * FROM hotel_branches WHERE branch_id = ?", mapper, id);
			return Optional.ofNullable(branch);
		} catch (EmptyResultDataAccessException ex) {
			return Optional.empty();
		}
	}

	
	@Override
	public List<Branch> findAll() {
		return jdbcTemplate.query("SELECT * FROM hotel_branches ORDER BY branch_id", mapper);
	}


	@Override
	public List<Branch> findByLocation(String location) {
		return jdbcTemplate.query("SELECT * FROM hotel_branches WHERE location LIKE ?", mapper, "%" + location + "%");
	}


	@Override
	public void update(Branch branch) {
		jdbcTemplate.update(
				"UPDATE hotel_branches SET name=?, location=?, contact_number=?, manager_id=?, total_rooms=?, status=? WHERE branch_id=?",
				branch.getName(), branch.getLocation(), branch.getContactNumber(), branch.getManagerId(),
				branch.getTotalRooms(), branch.getStatus(), branch.getBranchId());
	}


	@Override
	public void toggleStatus(Integer id) {
		String sql = """
				UPDATE hotel_branches
				SET status = CASE
				                WHEN status = 'active' THEN 'inactive'
				                ELSE 'active'
				             END
				WHERE branch_id = ?
				""";

		jdbcTemplate.update(sql, id);
	}
}
