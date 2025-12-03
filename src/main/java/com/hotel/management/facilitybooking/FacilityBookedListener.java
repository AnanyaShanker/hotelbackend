//package com.hotel.management.facilitybooking;
//
//import com.hotel.management.notifications.NotificationService;
//import com.hotel.management.notifications.NotificationType;
//import com.hotel.management.facilities.FacilityService;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Map;
//
//@Component
//public class FacilityBookedListener {
//
//    private final NotificationService notificationService;
//    private final FacilityService facilityService;
//
//    public FacilityBookedListener(NotificationService notificationService,
//                                  FacilityService facilityService) {
//        this.notificationService = notificationService;
//        this.facilityService = facilityService;
//    }
//
//    @EventListener
//    @Transactional
//    public void onFacilityBooked(FacilityBookedEvent event) throws Exception {
//        FacilityBooking booking = event.getBooking();
//
//        // Fetch facility from DB
//        var facility = facilityService.getFacilityById(booking.getFacilityId());
//
//        // Get facility name
//        String facilityName = facility.getName();
//
//        // Send notification
//        notificationService.sendNotification(
//                booking.getCustomerId(),
//                NotificationType.FACILITY_BOOKING_CONFIRMATION,
//                Map.of(
//                        "facilityName", facilityName,
//                        "date", booking.getBookingDate(),
//                        "startTime", booking.getStartTime(),
//                        "endTime", booking.getEndTime(),
//                        "quantity", String.valueOf(booking.getQuantity()),
//                        "totalPrice", String.valueOf(booking.getTotalPrice())
//                )
//        );
//    }
//}
