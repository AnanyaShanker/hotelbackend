package com.hotel.management.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.management.users.User;
import com.hotel.management.users.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

	@Autowired
    private StaffRepository staffRepository;
	
	@Autowired
    private  UserRepository userRepository; 

  

    @Override
    @Transactional
    public StaffDTO createStaff(Integer userId, Integer hotelId) {
       
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        if (user.getRoleId() != 2) {
            throw new IllegalStateException("User is not staff role (role_id must be 2)");
        }

       
        Staff existing = staffRepository.findByUserId(userId).orElse(null);
        if (existing != null) {
            return StaffMapper.toDTO(existing);
        }

        Staff staff = new Staff();
        staff.setUserId(userId);
        staff.setHotelId(hotelId);
        staff.setStatus("AVAILABLE");

        int id = staffRepository.save(staff);
        staff.setStaffId(id);
        return StaffMapper.toDTO(staff);
    }

    @Override
    public StaffDTO getById(int staffId) {
        Staff s = staffRepository.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found: " + staffId));
        return StaffMapper.toDTO(s);
    }

    @Override
    public StaffDTO getByUserId(int userId) {
        Staff s = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found for user: " + userId));
        return StaffMapper.toDTO(s);
    }

    @Override
    public List<StaffDTO> list(Integer hotelId, String status) {
        return staffRepository.findAll(hotelId, status).stream()
                .map(StaffMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StaffDTO updateHotel(int staffId, int hotelId) {
        if (!staffRepository.existsById(staffId)) {
            throw new IllegalArgumentException("Staff not found: " + staffId);
        }
        staffRepository.updateHotel(staffId, hotelId);
        return getById(staffId);
    }

    @Override
    @Transactional
    public StaffDTO updateStatus(int staffId, String status) {
        if (!"AVAILABLE".equals(status) && !"UNAVAILABLE".equals(status)) {
            throw new IllegalArgumentException("Invalid status. Use AVAILABLE or UNAVAILABLE");
        }
        if (!staffRepository.existsById(staffId)) {
            throw new IllegalArgumentException("Staff not found: " + staffId);
        }
        staffRepository.updateStatus(staffId, status);
        return getById(staffId);
    }
}
