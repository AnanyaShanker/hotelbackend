package com.hotel.management.roles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepo;

	@Override
	public RoleDTO createRole(RoleDTO dto) {
		Role role = RoleMapper.toModel(dto);
		int id = roleRepo.save(role);
		role.setRoleId(id);
		return RoleMapper.toDTO(role);
	}

	@Override
	public List<RoleDTO> getAllRoles() {
		return roleRepo.findAll().stream().map(RoleMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public RoleDTO getRoleById(int id) {
		Role role = roleRepo.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
		return RoleMapper.toDTO(role);
	}

	@Override
	public RoleDTO updateRole(int id, RoleDTO dto) {
		Role role = roleRepo.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));

		role.setRoleName(dto.getRoleName());
		role.setDescription(dto.getDescription());

		roleRepo.update(role);
		return RoleMapper.toDTO(role);
	}

	@Override
	public void deleteRole(int id) {
		roleRepo.delete(id);
	}
}
