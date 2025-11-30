package com.hotel.management.media;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MediaService {

    @Value("${media.upload.dir}")
    private String uploadDir;

    public MediaUploadRequest handleFileUpload(
            MultipartFile file,
            String ownerType,
            Integer ownerId,
            Integer uploadedBy
    ) throws Exception {

        
        File folder = new File(uploadDir);
        if (!folder.exists()) folder.mkdirs();

        
        String originalName = file.getOriginalFilename();
        String uniqueName = System.currentTimeMillis() + "_" + originalName;

        
        Path path = Paths.get(uploadDir,uniqueName);

        
        Files.write(path, file.getBytes());

        
        MediaUploadRequest req = new MediaUploadRequest();
        req.setOwnerType(ownerType);
        req.setOwnerId(ownerId);
        req.setFileName(uniqueName);
        req.setFileType(file.getContentType());
        req.setFileSize(file.getSize());
        req.setFilePath(path.toString());
        req.setUploadedBy(uploadedBy);

        return req;
    }
}
