	package com.hotel.management.bookings;
	
	import com.hotel.management.payment.Payment;
	import com.hotel.management.rooms.Room;
	import com.hotel.management.rooms.RoomService;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;

    import java.sql.Timestamp;
    import java.util.List;
	
	@Service
	public class BookingsServiceImpl implements BookingsService {
		@Autowired
	    private  BookingsRepository bookingsRepository;
		@Autowired
	    private RoomService roomService;
		@Autowired
		private org.springframework.context.ApplicationEventPublisher eventPublisher;
	
	
		@Override
		public Bookings createBooking(Bookings booking, int branchId, int typeId) {

		    List<Room> availableRooms = roomService.getAvailableRooms(branchId, typeId);
		    if (availableRooms.isEmpty()) {
		        throw new IllegalStateException("No available rooms for branch " + branchId + " and type " + typeId);
		    }

		    Room room = availableRooms.get(0);

		    booking.setRoomId(room.getRoomId());
		    booking.setBranchId(branchId);
		    booking.setBookingStatus("CONFIRMED");  // customer booking created before payment
		    booking.setTotalDays(calculateDays(booking.getCheckInDate(), booking.getCheckOutDate()));
		    booking.setTotalPrice(room.getPricePerNight() * booking.getTotalDays());

		    Bookings saved = bookingsRepository.save(booking);

		    // Set room to OCCUPIED
		    roomService.changeRoomStatus(room.getRoomId(), "OCCUPIED");

		 
		    eventPublisher.publishEvent(new BookingCreatedEvent(saved));

		    return saved;
		}
		
		 private long calculateDays(Timestamp checkIn, Timestamp checkOut) {
		        long diffMillis = checkOut.getTime() - checkIn.getTime();
		        return diffMillis / (1000 * 60 * 60 * 24);
		    }
	
	    @Override
	    public Bookings getBookingById(int bookingId) {
	        return bookingsRepository.findById(bookingId);
	    }
	
	    @Override
	    public List<Bookings> getAllBookings() {
	        return bookingsRepository.findAll();
	    }
	
	    @Override
	    public List<Bookings> getBookingsByBranch(int branchId) {
	        return bookingsRepository.findByBranch(branchId);
	    }
	
	    @Override
	    public void cancelBooking(int bookingId) {
	        Bookings booking = bookingsRepository.findById(bookingId);
	        bookingsRepository.updateStatus(bookingId, "CANCELLED");
	        roomService.changeRoomStatus(booking.getRoomId(), "AVAILABLE");
	    }
	
	    @Override
	    public void completeBooking(int bookingId) {
	        Bookings booking = bookingsRepository.findById(bookingId);
	        bookingsRepository.updateStatus(bookingId, "COMPLETED");
	        roomService.changeRoomStatus(booking.getRoomId(), "AVAILABLE");
	    }
	
	    @Override
	    public void updateBookingStatusBasedOnPayment(Payment payment) {
	        if (payment.getBookingId() == null) return;
	
	        if ("SUCCESS".equalsIgnoreCase(payment.getStatus())) {
	            bookingsRepository.updateStatus(payment.getBookingId(), "CONFIRMED");
	        } else {
	            bookingsRepository.updateStatus(payment.getBookingId(), "CANCELLED");
	            Bookings booking = bookingsRepository.findById(payment.getBookingId());
	            roomService.changeRoomStatus(booking.getRoomId(), "AVAILABLE");
	        }
	    }
	}
