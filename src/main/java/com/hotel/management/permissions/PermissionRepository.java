package com.hotel.management.permissions;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository {

	int save(Permission permission);

	Optional<Permission> findById(int id);

	List<Permission> findAll();

	void  update(Permission permission);

	void  delete(int id);
}