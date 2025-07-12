package com.cvanalyzer.dto;

import com.cvanalyzer.model.ParseStatus;
import java.time.LocalDateTime;

public class ResumeUploadResponse {
    private Long id;
    private String fileName;
    private Long fileSize;
    private ParseStatus status;
    private LocalDateTime uploadDate;
    private String message;

    // Constructors
    public ResumeUploadResponse() {}

    public ResumeUploadResponse(Long id, String fileName, Long fileSize,
                                ParseStatus status, LocalDateTime uploadDate, String message) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.status = status;
        this.uploadDate = uploadDate;
        this.message = message;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public ParseStatus getStatus() { return status; }
    public void setStatus(ParseStatus status) { this.status = status; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
