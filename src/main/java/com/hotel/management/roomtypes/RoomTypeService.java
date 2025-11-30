package com.hotel.management.roomtypes;

import java.util.List;

public interface RoomTypeService {
    List<RoomTypes> getRoom();
    RoomTypes getRoomById(Integer id);
    RoomTypes addRoom(RoomTypes dto);
    RoomTypes updateRoom(Integer id, RoomTypes dto);

}