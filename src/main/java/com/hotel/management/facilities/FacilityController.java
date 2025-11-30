package com.hotel.management.facilities;

import com.hotel.management.facilities.FacilityResponseDTO;
import com.hotel.management.media.Media;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<String> addFacility(@RequestBody Facility facility) {
        int rows = facilityService.addFacility(facility);
        return ResponseEntity.ok(rows > 0 ? "Facility added successfully" : "Failed to add facility");
    }

    // GET ALL (DTO LIST)
    @GetMapping("/all")
    public List<FacilityResponseDTO> getAll() {
        return facilityService.getAllFacilities()
                .stream()
                .map(facilityService::buildResponseDTO)
                .collect(Collectors.toList());
    }

    // GET BY ID (DTO)
    @GetMapping("/{id}")
    public FacilityResponseDTO getById(@PathVariable int id) {
        Facility f = facilityService.getFacilityById(id);
        return facilityService.buildResponseDTO(f);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Facility facility) {
        facility.setFacilityId(id);
        boolean updated = facilityService.updateFacility(facility);
        return ResponseEntity.ok(updated ? "Updated" : "Update failed");
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        boolean deleted = facilityService.deleteFacility(id);
        return ResponseEntity.ok(deleted ? "Deleted" : "Delete failed");
    }

    // ============= UPLOADS =============

    @PostMapping("/{id}/primary-image")
    public ResponseEntity<String> uploadPrimaryImage(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "uploadedBy", required = false) Integer uploadedBy
    ) {
        try {
            String path = facilityService.uploadAndSetPrimaryImage(id, file, uploadedBy);
            return ResponseEntity.ok("Primary image uploaded: " + path);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/brochure")
    public ResponseEntity<String> uploadBrochure(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "uploadedBy", required = false) Integer uploadedBy
    ) {
        try {
            String path = facilityService.uploadAndSetBrochure(id, file, uploadedBy);
            return ResponseEntity.ok("Brochure uploaded: " + path);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/gallery")
    public ResponseEntity<String> uploadGallery(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "uploadedBy", required = false) Integer uploadedBy
    ) {
        try {
            int mediaId = facilityService.uploadGalleryImage(id, file, uploadedBy);
            return ResponseEntity.ok("Gallery image uploaded with media_id = " + mediaId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/{id}/gallery/multiple")
    public ResponseEntity<String> uploadGalleryMultiple(
            @PathVariable int id,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "uploadedBy", required = false) Integer uploadedBy
    ) {
        try {
            List<Integer> mediaIds = files.stream()
                    .map(file -> {
                        try {
                            return facilityService.uploadGalleryImage(id, file, uploadedBy);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed for file: " + file.getOriginalFilename(), e);
                        }
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok("Gallery images uploaded with media_ids = " + mediaIds);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }


    @GetMapping("/{id}/gallery")
    public List<Media> getGallery(@PathVariable int id) {
        return facilityService.getGallery(id);
    }
}
