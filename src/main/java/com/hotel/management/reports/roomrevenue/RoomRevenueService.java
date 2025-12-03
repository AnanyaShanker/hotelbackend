package com.hotel.management.reports.roomrevenue;

import java.util.List;

public interface RoomRevenueService {

    List<RoomRevenueDto> getOverallRevenueByRoomType();

    List<RoomRevenueDto> getRevenueByRoomTypeForManager(Integer managerUserId);
}
