package com.hotel.management.rooms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
    private RoomRepository roomRepository;


    @Override
    public Room createRoom(Room room) {
       
        if (room.getStatus() == null || room.getStatus().isEmpty()) {
            room.setStatus("AVAILABLE");
        }
        return roomRepository.save(room);
    }
    
    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> getAllAvailableRooms() {
        return roomRepository.findAllAvailable();
    }
    

    @Override
    public Room getRoomById(int roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public List<Room> getRoomsByBranch(int branchId) {
        return roomRepository.findByBranch(branchId);
    }

    @Override
    public List<Room> getRoomsByType(int typeId) {
        return roomRepository.findByType(typeId);
    }

    @Override
    public List<Room> getAvailableRooms(int branchId, int typeId) {
        
        return roomRepository.findAvailableRooms(branchId, typeId);
    }

    @Override
    public List<Room> getAvailableRoomsForDates(int branchId, int typeId, java.sql.Timestamp checkIn, java.sql.Timestamp checkOut) {
        return roomRepository.findAvailableRoomsForDates(branchId, typeId, checkIn, checkOut);
    }

    @Override
    public Room updateRoom(Room room) {
        return roomRepository.update(room);
    }

    @Override
    public void softDeleteRoom(int roomId) {
       
        roomRepository.softDelete(roomId);
    }

    @Override
    public void changeRoomStatus(int roomId, String status) {
        roomRepository.updateStatus(roomId, status);
    }
}
