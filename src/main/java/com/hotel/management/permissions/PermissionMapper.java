package com.hotel.management.permissions;

import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

	public static Permission toModel(PermissionDTO dto) {
		Permission p = new Permission();
		p.setPermissionId(dto.getPermissionId());
		p.setPermissionName(dto.getPermissionName());
		p.setDescription(dto.getDescription());
		return p;
	}

	public static PermissionDTO toDTO(Permission p) {
		PermissionDTO dto = new PermissionDTO();
		dto.setPermissionId(p.getPermissionId());
		dto.setPermissionName(p.getPermissionName());
		dto.setDescription(p.getDescription());
		return dto;
	}
}