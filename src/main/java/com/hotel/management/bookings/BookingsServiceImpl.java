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

	    //  VALIDATION: Check dates are valid
	    if (booking.getCheckInDate() == null || booking.getCheckOutDate() == null) {
	        throw new IllegalArgumentException("Check-in and check-out dates are required");
	    }

	    if (booking.getCheckOutDate().before(booking.getCheckInDate()) ||
	        booking.getCheckOutDate().equals(booking.getCheckInDate())) {
	        throw new IllegalArgumentException("Check-out date must be after check-in date");
	    }

	    // Prevent booking in the past
	    Timestamp now = new Timestamp(System.currentTimeMillis());
	    if (booking.getCheckInDate().before(now)) {
	        throw new IllegalArgumentException("Cannot book rooms in the past");
	    }

	    //  Check room availability for the specific date range (prevents double-booking)
	    List<Room> availableRooms = roomService.getAvailableRoomsForDates(
	        branchId,
	        typeId,
	        booking.getCheckInDate(),
	        booking.getCheckOutDate()
	    );

	    if (availableRooms.isEmpty()) {
	        throw new IllegalStateException(
	            "No available rooms for branch " + branchId +
	            " and type " + typeId +
	            " from " + booking.getCheckInDate() +
	            " to " + booking.getCheckOutDate()
	        );
	    }

		    Room room = availableRooms.get(0);

		    booking.setRoomId(room.getRoomId());
		    booking.setBranchId(branchId);
		    booking.setBookingStatus("CONFIRMED");

		    // Calculate total price based on days and price per night
		    long totalDays = calculateDays(booking.getCheckInDate(), booking.getCheckOutDate());
		    booking.setTotalPrice(room.getPricePerNight() * totalDays);

		    Bookings saved = bookingsRepository.save(booking);

		    // Fetch complete booking with generated ID
		    Bookings complete = bookingsRepository.findById(saved.getBookingId());

		    // NOTE: Room status remains "AVAILABLE" - the booking record blocks the dates
		    // This allows the same room to be booked for different, non-overlapping date ranges

		    eventPublisher.publishEvent(new BookingCreatedEvent(complete));

		    return complete;
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
    public List<Bookings> getBookingsByCustomer(int customerId) {
        return bookingsRepository.findByCustomer(customerId);
    }

    @Override
    public void updatePaymentStatus(int bookingId, String paymentStatus) {
        bookingsRepository.updatePaymentStatus(bookingId, paymentStatus);
    }

    @Override
    public BookingsDTO getBookingDetails(int bookingId) {
        Bookings booking = bookingsRepository.findById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found with ID: " + bookingId);
        }

        Room room = roomService.getRoomById(booking.getRoomId());

        BookingsDTO dto = new BookingsDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setCustomerId(booking.getCustomerId());
        dto.setRoomId(booking.getRoomId());
        dto.setBranchId(booking.getBranchId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setPaymentStatus(booking.getPaymentStatus());
        dto.setBookingStatus(booking.getBookingStatus());
        dto.setNotes(booking.getNotes());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());

        if (room != null) {
            dto.setRoomNumber(room.getRoomNumber());
            dto.setPricePerNight(room.getPricePerNight());
        }

        return dto;
    }

    @Override
    public void cancelBooking(int bookingId) {
	        // Simply update booking status to CANCELLED
	        // No need to change room status - date-based availability handles this
	        bookingsRepository.updateStatus(bookingId, "CANCELLED");
	    }
	
	    @Override
	    public void completeBooking(int bookingId) {
	        // Simply update booking status to COMPLETED
	        // No need to change room status - date-based availability handles this
	        bookingsRepository.updateStatus(bookingId, "COMPLETED");
	    }
	    @Override
	    public List<ManagerBookingViewDTO> getDetailedBookingsForBranch(int branchId) {
	        // ✅ This is the missing method
	        // Example: query DB for bookings with extra manager details
	        // return managerBookingRepository.findDetailedByBranch(branchId);
	        return null;
	    }
    @Override
    public void updateBookingStatusBasedOnPayment(Payment payment) {
        if (payment.getBookingId() == null) return;

        if ("SUCCESS".equalsIgnoreCase(payment.getStatus())) {
            bookingsRepository.updateStatus(payment.getBookingId(), "CONFIRMED");
        } else {
            // Cancel booking if payment fails
            // Room becomes available automatically via date-based query
            bookingsRepository.updateStatus(payment.getBookingId(), "CANCELLED");
        }
    }
	}
