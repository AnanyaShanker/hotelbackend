package com.hotel.management.roomtypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoomTypeRepositoryImpl implements RoomTypeRepository {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	@Autowired
    private  RoomTypeRowMapper rowMapper = new RoomTypeRowMapper();

    @Override
    public List<RoomTypes> findAll() {
        return jdbcTemplate.query("SELECT type_id, type_name, description FROM room_types", rowMapper);
    }

    @Override
    public Optional<RoomTypes> findById(Integer id) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT type_id, type_name, description FROM room_types WHERE type_id= ?", rowMapper, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Integer insert(RoomTypes dto) {
        jdbcTemplate.update("INSERT INTO room_types(type_name, description) VALUES (?, ?)",
                dto.getTypeName(), dto.getDescription());
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    @Override
    public int update(Integer id, RoomTypes dto) {
        return jdbcTemplate.update("UPDATE room_types SET type_name = ?, description = ? WHERE type_id = ?",
                dto.getTypeName(), dto.getDescription(), id);
    }


}
