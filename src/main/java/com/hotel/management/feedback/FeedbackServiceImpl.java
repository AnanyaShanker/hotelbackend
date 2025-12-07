package com.hotel.management.feedback;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    // --------------- CRUD ---------------

    @Override
    public FeedbackDto createFeedback(FeedbackDto dto) {

        // Business rule: exactly ONE of bookingId or facilityBookingId should be set
        boolean hasBooking = dto.getBookingId() != null;
        boolean hasFacility = dto.getFacilityBookingId() != null;

        if (hasBooking == hasFacility) { // both true or both false
            throw new IllegalArgumentException(
                    "Feedback must be linked to EITHER a room booking OR a facility booking (exactly one).");
        }

        Feedback entity = toEntity(dto);
        Integer id = feedbackRepository.addFeedback(entity);
        entity.setFeedbackId(id);

        return toDto(entity);
    }

    @Override
    public FeedbackDto getById(Integer id) {
        Feedback entity = feedbackRepository.getById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id " + id));
        return toDto(entity);
    }

    @Override
    public List<FeedbackDto> getAll() {
        return feedbackRepository.getAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackDto> getByCustomer(Integer customerId) {
        return feedbackRepository.getByCustomer(customerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackDto> getByBooking(Integer bookingId) {
        return feedbackRepository.getByBooking(bookingId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackDto> getByFacilityBooking(Integer facilityBookingId) {
        return feedbackRepository.getByFacilityBooking(facilityBookingId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FeedbackDto update(Integer id, FeedbackDto dto) {
        Feedback existing = feedbackRepository.getById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id " + id));

        if (dto.getRating() != null) existing.setRating(dto.getRating());
        if (dto.getComments() != null) existing.setComments(dto.getComments());

        feedbackRepository.updateFeedback(id, existing);
        return toDto(existing);
    }

    @Override
    public void delete(Integer id) {
        feedbackRepository.deleteFeedback(id);
    }

    // --------------- Admin view ---------------

    @Override
    public List<FeedbackDisplayDto> getAdminDisplayList() {
        return feedbackRepository.getAdminDisplayList();
    }

    // --------------- Helpers ---------------

    private FeedbackDto toDto(Feedback entity) {
        return new FeedbackDto(
                entity.getFeedbackId(),
                entity.getCustomerId(),
                entity.getBookingId(),
                entity.getFacilityBookingId(),
                entity.getRating(),
                entity.getComments(),
                entity.getSubmissionDate()
        );
    }

    private Feedback toEntity(FeedbackDto dto) {
        return new Feedback(
                dto.getFeedbackId(),
                dto.getCustomerId(),
                dto.getBookingId(),
                dto.getFacilityBookingId(),
                dto.getRating(),
                dto.getComments(),
                dto.getSubmissionDate()
        );
    }
}