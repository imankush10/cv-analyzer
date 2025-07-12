package com.cvanalyzer.repository;

import com.cvanalyzer.model.Resume;
import com.cvanalyzer.model.ParseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    // Find resumes by status
    List<Resume> findByStatus(ParseStatus status);

    // Find resumes by candidate name (case-insensitive)
    List<Resume> findByCandidateNameContainingIgnoreCase(String candidateName);

    // Find resumes by email
    List<Resume> findByEmail(String email);

    // Custom query to get recent uploads
    @Query("SELECT r FROM Resume r ORDER BY r.uploadDate DESC")
    List<Resume> findRecentUploads();
}
