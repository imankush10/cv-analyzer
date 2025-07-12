package com.cvanalyzer.controller;

import com.cvanalyzer.dto.ResumeUploadResponse;
import com.cvanalyzer.model.Resume;
import com.cvanalyzer.model.ParseStatus;
import com.cvanalyzer.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.cvanalyzer.model.ResumeSkill;
import com.cvanalyzer.service.SkillExtractionService;
import com.cvanalyzer.repository.ResumeSkillRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "http://localhost:5173") // For React frontend
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private SkillExtractionService skillExtractionService;

    @Autowired
    private ResumeSkillRepository resumeSkillRepository;

    @PostMapping("/upload")
    public ResponseEntity<ResumeUploadResponse> uploadResume(
            @RequestParam("file") MultipartFile file) {

        ResumeUploadResponse response = resumeService.uploadResume(file);

        if (response.getId() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<Resume>> getAllResumes() {
        List<Resume> resumes = resumeService.getAllResumes();
        return ResponseEntity.ok(resumes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResumeById(@PathVariable Long id) {
        Optional<Resume> resume = resumeService.getResumeById(id);

        if (resume.isPresent()) {
            return ResponseEntity.ok(resume.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Resume>> getResumesByStatus(@PathVariable ParseStatus status) {
        List<Resume> resumes = resumeService.getResumesByStatus(status);
        return ResponseEntity.ok(resumes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResume(@PathVariable Long id) {
        boolean deleted = resumeService.deleteResume(id);

        if (deleted) {
            return ResponseEntity.ok("Resume deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Resume not found or could not be deleted");
        }
    }
    @PostMapping("/{id}/extract-skills")
    public ResponseEntity<List<ResumeSkill>> extractSkills(@PathVariable Long id) {
        Optional<Resume> resume = resumeService.getResumeById(id);

        if (resume.isPresent()) {
            List<ResumeSkill> extractedSkills = skillExtractionService.extractSkillsFromResume(resume.get());
            return ResponseEntity.ok(extractedSkills);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/skills")
    public ResponseEntity<List<ResumeSkill>> getResumeSkills(@PathVariable Long id) {
        List<ResumeSkill> skills = resumeSkillRepository.findByResumeId(id);
        return ResponseEntity.ok(skills);
    }
}
