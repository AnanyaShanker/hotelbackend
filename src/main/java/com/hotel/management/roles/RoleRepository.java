package com.hotel.management.roles;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

	int save(Role role);

	List<Role> findAll();

	Optional<Role> findById(int id);

	int update(Role role);

	int delete(int id);
}
