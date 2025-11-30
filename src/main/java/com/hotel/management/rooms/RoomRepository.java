package com.hotel.management.rooms;

import java.util.List;

public interface RoomRepository {
    Room save(Room room);
    List<Room> findAll();
    List<Room> findAllAvailable();
    Room findById(int roomId);
    List<Room> findByBranch(int branchId);
    List<Room> findByType(int typeId);
    List<Room> findAvailableRooms(int branchId, int typeId);
    Room update(Room room);
    void softDelete(int roomId);
    void updateStatus(int roomId, String status);
}
