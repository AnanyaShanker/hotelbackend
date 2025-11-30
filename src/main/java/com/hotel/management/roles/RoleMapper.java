package com.hotel.management.roles;

public class RoleMapper {

	public static Role toModel(RoleDTO dto) {
		 Role role = new Role();
		 role.setRoleId(dto.getRoleId());
		 role.setRoleName(dto.getRoleName());
		 role.setDescription(dto.getDescription());
		 return role;
		 }

		 public static RoleDTO toDTO(Role role) {
		 RoleDTO dto = new RoleDTO();
		 dto.setRoleId(role.getRoleId());
		 dto.setRoleName(role.getRoleName());
		 dto.setDescription(role.getDescription());
		 return dto;
		 }
}
