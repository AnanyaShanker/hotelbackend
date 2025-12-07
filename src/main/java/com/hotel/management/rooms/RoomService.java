package com.hotel.management.rooms;

import java.sql.Timestamp;
import java.util.List;

public interface RoomService {
    Room createRoom(Room room);
    List<Room> getAllRooms();
    List<Room> getAllAvailableRooms();
    Room getRoomById(int roomId);
    List<Room> getRoomsByBranch(int branchId);
    List<Room> getRoomsByType(int typeId);
    List<Room> getAvailableRooms(int branchId, int typeId);

    
    List<Room> getAvailableRoomsForDates(int branchId, int typeId, Timestamp checkIn, Timestamp checkOut);

    Room updateRoom(Room room);
    void softDeleteRoom(int roomId);
    void changeRoomStatus(int roomId, String status);
}
