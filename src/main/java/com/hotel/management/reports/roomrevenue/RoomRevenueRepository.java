package com.hotel.management.reports.roomrevenue;

import java.util.List;

public interface RoomRevenueRepository {

    /**
     * Overall revenue (all branches) grouped by room type.
     */
    List<RoomRevenueDto> getRevenueByRoomType();

    /**
     * Revenue grouped by room type for branches managed by given manager.
     */
    List<RoomRevenueDto> getRevenueByRoomTypeForManager(Integer managerUserId);
}

