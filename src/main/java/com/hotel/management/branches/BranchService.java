package com.hotel.management.branches;

import java.util.List;

public interface BranchService {

	BranchResponseDTO addBranch(BranchRequestDTO request);

	BranchResponseDTO getBranchById(Integer id);

	List<BranchResponseDTO> getAllBranches();

	List<BranchResponseDTO> getBranchesByLocation(String location);

	BranchResponseDTO updateBranch(Integer id, BranchRequestDTO request);

	void toggleStatus(Integer id);
}
