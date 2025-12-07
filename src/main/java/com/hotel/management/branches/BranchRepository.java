package com.hotel.management.branches;

import java.util.List;
import java.util.Optional;

public interface BranchRepository {

    Integer addBranch(Branch branch);

    Optional<Branch> findById(Integer id);

    Optional<Branch> findByManagerId(Integer managerId);  // ⭐ NEW

    List<Branch> findAll();

    List<Branch> findByLocation(String location);

    void update(Branch branch);

    void toggleStatus(Integer id);
}
