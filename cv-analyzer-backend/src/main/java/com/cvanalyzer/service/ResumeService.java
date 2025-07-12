package com.cvanalyzer.service;

import com.cvanalyzer.dto.ResumeUploadResponse;
import com.cvanalyzer.model.Resume;
import com.cvanalyzer.model.ParseStatus;
import com.cvanalyzer.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.Map;
import org.springframework.scheduling.annotation.Async;


@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private TextExtractionService textExtractionService;

    @Autowired
    private NLPProcessingService nlpProcessingService;

    private final String UPLOAD_DIR = "uploads/resumes/";

    @Async
    public CompletableFuture<Void> processResumeAsync(Long resumeId) {
        try {
            Optional<Resume> resumeOpt = resumeRepository.findById(resumeId);
            if (resumeOpt.isEmpty()) {
                return CompletableFuture.completedFuture(null);
            }

            Resume resume = resumeOpt.get();
            resume.setStatus(ParseStatus.PROCESSING);
            resumeRepository.save(resume);

            // Extract text from file
            String extractedText = textExtractionService.extractTextFromFile(resume.getFilePath());
            resume.setExtractedText(extractedText);

            // Process with NLP
            Map<String, Object> nlpResults = nlpProcessingService.processResumeText(extractedText);

            // Update resume with extracted information
            if (nlpResults.get("candidateName") != null) {
                resume.setCandidateName((String) nlpResults.get("candidateName"));
            }
            if (nlpResults.get("email") != null) {
                resume.setEmail((String) nlpResults.get("email"));
            }
            if (nlpResults.get("phoneNumber") != null) {
                resume.setPhoneNumber((String) nlpResults.get("phoneNumber"));
            }

            resume.setStatus(ParseStatus.COMPLETED);
            resumeRepository.save(resume);

            System.out.println("Resume processing completed for ID: " + resumeId);
            System.out.println("Extracted skills: " + nlpResults.get("technicalSkills"));

        } catch (Exception e) {
            // Handle processing failure
            Optional<Resume> resumeOpt = resumeRepository.findById(resumeId);
            if (resumeOpt.isPresent()) {
                Resume resume = resumeOpt.get();
                resume.setStatus(ParseStatus.FAILED);
                resumeRepository.save(resume);
            }
            System.err.println("Resume processing failed for ID: " + resumeId + ", Error: " + e.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    public ResumeUploadResponse uploadResume(MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return new ResumeUploadResponse(null, null, null, null, null, "File is empty");
            }

            // Check file type
            String contentType = file.getContentType();
            if (!isValidFileType(contentType)) {
                return new ResumeUploadResponse(null, null, null, null, null,
                        "Invalid file type. Only PDF, DOC, and DOCX files are allowed.");
            }

            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension;

            // Save file
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Create resume entity
            Resume resume = new Resume(originalFilename, filePath.toString(), file.getSize());
            resume.setStatus(ParseStatus.PENDING);

            // Save to database
            Resume savedResume = resumeRepository.save(resume);

            processResumeAsync(savedResume.getId());

            return new ResumeUploadResponse(
                    savedResume.getId(),
                    savedResume.getFileName(),
                    savedResume.getFileSize(),
                    savedResume.getStatus(),
                    savedResume.getUploadDate(),
                    "Resume uploaded successfully! Processing started."
            );

        } catch (IOException e) {
            return new ResumeUploadResponse(null, null, null, null, null,
                    "Failed to upload file: " + e.getMessage());
        }
    }

    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }

    public Optional<Resume> getResumeById(Long id) {
        return resumeRepository.findById(id);
    }

    public List<Resume> getResumesByStatus(ParseStatus status) {
        return resumeRepository.findByStatus(status);
    }

    public boolean deleteResume(Long id) {
        try {
            Optional<Resume> resume = resumeRepository.findById(id);
            if (resume.isPresent()) {
                // Delete file from filesystem
                Path filePath = Paths.get(resume.get().getFilePath());
                Files.deleteIfExists(filePath);

                // Delete from database
                resumeRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidFileType(String contentType) {
        return contentType != null && (
                contentType.equals("application/pdf") ||
                        contentType.equals("application/msword") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        );
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
