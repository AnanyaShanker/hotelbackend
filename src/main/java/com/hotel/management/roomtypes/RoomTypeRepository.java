package com.hotel.management.roomtypes;

import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository {
    List<RoomTypes> findAll();
    Optional<RoomTypes> findById(Integer id);
    Integer insert(RoomTypes dto);
    int update(Integer id, RoomTypes dto);

}
