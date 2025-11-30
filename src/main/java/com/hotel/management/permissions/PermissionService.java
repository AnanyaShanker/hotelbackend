package com.hotel.management.permissions;

import java.util.List;
import java.util.Optional;

public interface PermissionService {

	PermissionDTO create(PermissionDTO dto);

	List<PermissionDTO> getAll();

	PermissionDTO getById(int id);

	PermissionDTO update(int id,PermissionDTO dto);

	void delete(int id);
}
