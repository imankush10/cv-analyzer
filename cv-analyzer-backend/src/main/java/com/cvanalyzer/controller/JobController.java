package com.cvanalyzer.controller;

import com.cvanalyzer.model.JobDescription;
import com.cvanalyzer.service.JobService;
import com.cvanalyzer.dto.JobMatchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:5173")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<JobDescription> createJob(@RequestBody JobDescription job) {
        JobDescription savedJob = jobService.saveJob(job);
        return ResponseEntity.ok(savedJob);
    }

    @GetMapping
    public ResponseEntity<List<JobDescription>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDescription> getJobById(@PathVariable Long id) {
        Optional<JobDescription> job = jobService.getJobById(id);
        return job.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{jobId}/match/{resumeId}")
    public ResponseEntity<JobMatchResponse> matchResumeToJob(
            @PathVariable Long jobId, @PathVariable Long resumeId) {
        JobMatchResponse match = jobService.calculateMatch(jobId, resumeId);
        return ResponseEntity.ok(match);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        boolean deleted = jobService.deleteJob(id);
        return deleted ? ResponseEntity.ok("Job deleted successfully")
                : ResponseEntity.notFound().build();
    }
}
