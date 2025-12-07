package com.hotel.management.exception;

/**
 * Exception thrown for file upload/download failures
 */
public class FileStorageException extends RuntimeException {

    private String fileName;
    private String operation;

    public FileStorageException(String fileName, String operation, String reason) {
        super(String.format("File %s failed for '%s': %s", operation, fileName, reason));
        this.fileName = fileName;
        this.operation = operation;
    }

    public FileStorageException(String message) {
        super(message);
    }

    public String getFileName() {
        return fileName;
    }

    public String getOperation() {
        return operation;
    }
}

