package com.hotel.management.branches;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BranchServiceImpl implements BranchService {

	private final BranchRepository branchRepository;

	public BranchServiceImpl(BranchRepository branchRepository) {
		this.branchRepository = branchRepository;
	}

	
	@Override
	public BranchResponseDTO addBranch(BranchRequestDTO request) {

		Branch branch = new Branch();

		branch.setName(request.getName());
		branch.setLocation(request.getLocation());
		branch.setContactNumber(request.getContactNumber());
		branch.setManagerId(request.getManagerId());
		branch.setTotalRooms(request.getTotalRooms());

		branch.setStatus(request.getStatus() == null ? "active" : request.getStatus());

		Integer generatedId = branchRepository.addBranch(branch);
		branch.setBranchId(generatedId);

		return convertToResponse(branch);
	}

	
	@Override
	public BranchResponseDTO getBranchById(Integer id) {

		Branch branch = branchRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Branch not found with ID " + id));

		return convertToResponse(branch);
	}
	
	@Override
	public BranchResponseDTO getBranchByManager(Integer managerId) {

	    Branch branch = branchRepository.findByManagerId(managerId)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "No branch found for manager with ID " + managerId));

	    return convertToResponse(branch);
	}

	
	@Override
	public List<BranchResponseDTO> getAllBranches() {

		return branchRepository.findAll().stream().map(this::convertToResponse).collect(Collectors.toList());
	}

	
	@Override
	public List<BranchResponseDTO> getBranchesByLocation(String location) {

		return branchRepository.findByLocation(location).stream().map(this::convertToResponse)
				.collect(Collectors.toList());
	}


	@Override
	public BranchResponseDTO updateBranch(Integer id, BranchRequestDTO request) {

		Branch existing = branchRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Branch not found with ID " + id));

		existing.setName(request.getName());
		existing.setLocation(request.getLocation());
		existing.setContactNumber(request.getContactNumber());
		existing.setManagerId(request.getManagerId());
		existing.setTotalRooms(request.getTotalRooms());

		if (request.getStatus() != null) {
			existing.setStatus(request.getStatus());
		}

		branchRepository.update(existing);

		return convertToResponse(existing);
	}


	@Override
	public void toggleStatus(Integer id) {

		Branch existing = branchRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Branch not found with ID " + id));

		branchRepository.toggleStatus(id);
	}


	private BranchResponseDTO convertToResponse(Branch branch) {
		BranchResponseDTO dto = new BranchResponseDTO();

		dto.setBranchId(branch.getBranchId());
		dto.setName(branch.getName());
		dto.setLocation(branch.getLocation());
		dto.setContactNumber(branch.getContactNumber());
		dto.setManagerId(branch.getManagerId());
		dto.setTotalRooms(branch.getTotalRooms());
		dto.setStatus(branch.getStatus());
		dto.setCreatedAt(branch.getCreatedAt());

		return dto;
	}
}