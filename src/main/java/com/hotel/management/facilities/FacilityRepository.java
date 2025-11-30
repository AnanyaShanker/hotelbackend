package com.hotel.management.facilities;

public interface FacilityRepository {
    Facility findById(int id);
    int save(Facility facility);
    boolean update(Facility facility);
    boolean delete(int id);
    boolean updatePrimaryImage(int facilityId, String path);
    boolean updateBrochure(int facilityId, String path);
    java.util.List<Facility> findAll();
}
