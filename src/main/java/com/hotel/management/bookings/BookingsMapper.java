package com.hotel.management.bookings;

import java.util.List;
import java.util.stream.Collectors;

public class BookingsMapper {

    public static BookingsDTO toDTO(Bookings booking) {
        BookingsDTO dto = new BookingsDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setCustomerId(booking.getCustomerId());
        dto.setRoomId(booking.getRoomId());
        dto.setBranchId(booking.getBranchId());
        dto.setBookingStatus(booking.getBookingStatus());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setNotes(booking.getNotes());
        return dto;
    }

   
    public static List<BookingsDTO> toDTOList(List<Bookings> bookings) {
        return bookings.stream()
                       .map(BookingsMapper::toDTO)
                       .collect(Collectors.toList());
    }
}
