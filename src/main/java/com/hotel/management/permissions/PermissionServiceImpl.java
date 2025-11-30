package com.hotel.management.permissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

	private final PermissionRepository permissionRepository;

	public PermissionServiceImpl(PermissionRepository permissionRepository) {
		this.permissionRepository = permissionRepository;
	}

	@Override
	public PermissionDTO create(PermissionDTO dto) {
		Permission p = PermissionMapper.toModel(dto);
		int id = permissionRepository.save(p);
		p.setPermissionId(id);
		return PermissionMapper.toDTO(p);
	}

	@Override
	public PermissionDTO getById(int id) {
		Permission p = permissionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
		return PermissionMapper.toDTO(p);
	}

	@Override
	public List<PermissionDTO> getAll() {
		return permissionRepository.findAll().stream().map(PermissionMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public PermissionDTO update(int id, PermissionDTO dto) {
		Permission existing = permissionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));

		existing.setPermissionName(dto.getPermissionName());
		existing.setDescription(dto.getDescription());
		permissionRepository.update(existing);

		return PermissionMapper.toDTO(existing);
	}

	@Override
	public void delete(int id) {
		permissionRepository.delete(id);
	}
}
