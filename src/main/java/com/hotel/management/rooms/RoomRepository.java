package com.hotel.management.rooms;

import java.sql.Timestamp;
import java.util.List;

public interface RoomRepository {
    Room save(Room room);
    List<Room> findAll();
    List<Room> findAllAvailable();
    Room findById(int roomId);
    List<Room> findByBranch(int branchId);
    List<Room> findByType(int typeId);
    List<Room> findAvailableRooms(int branchId, int typeId);

    
    List<Room> findAvailableRoomsForDates(int branchId, int typeId, Timestamp checkIn, Timestamp checkOut);

    Room update(Room room);
    void softDelete(int roomId);
    void updateStatus(int roomId, String status);
}
