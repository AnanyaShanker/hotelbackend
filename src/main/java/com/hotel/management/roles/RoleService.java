package com.hotel.management.roles;

import java.util.List;

public interface RoleService {

	RoleDTO createRole(RoleDTO dto);
	 List<RoleDTO> getAllRoles();
	 RoleDTO getRoleById(int id);
	 RoleDTO updateRole(int id, RoleDTO dto);
	 void deleteRole(int id);
}
