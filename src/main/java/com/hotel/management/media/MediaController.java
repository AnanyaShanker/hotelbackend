package com.hotel.management.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private MediaRepository mediaRepository;

    
    @PostMapping(consumes= MediaType.MULTIPART_FORM_DATA_VALUE, value= "/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("ownerType") String ownerType,
            @RequestParam("ownerId") Integer ownerId,
            @RequestParam("uploadedBy") Integer uploadedBy
    ) {
        try {
           
            MediaUploadRequest req = mediaService.handleFileUpload(
                    file, ownerType, ownerId, uploadedBy
            );

            // Save metadata in DB
            int id = mediaRepository.save(req);

            return "File uploaded successfully with media_id = " + id;

        } catch (Exception ex) {
            return "Upload failed: " + ex.getMessage();
        }
    }

    // -------------------- GET ALL --------------------
    @GetMapping("/all")
    public List<Media> getAllMedia() throws Exception {
        return mediaRepository.findAll();
    }

    // -------------------- GET BY ID --------------------
    @GetMapping("/{id}")
    public Media getMediaById(@PathVariable int id) throws Exception {
        return mediaRepository.findById(id);
    }

    // -------------------- DELETE --------------------
    @DeleteMapping("/{id}")
    public String deleteMedia(@PathVariable int id) throws Exception {
        boolean deleted = mediaRepository.delete(id);
        return deleted ? "Deleted successfully" : "Delete failed";
    }

    // -------------------- DOWNLOAD --------------------
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int id) throws Exception {
        Media media = mediaRepository.findById(id);
        if (media == null) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(media.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + media.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(media.getFileType()))
                .body(resource);
    }
}
