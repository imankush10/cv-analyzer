package com.cvanalyzer.service;

import com.cvanalyzer.model.JobDescription;
import com.cvanalyzer.model.Resume;
import com.cvanalyzer.model.ResumeSkill;
import com.cvanalyzer.dto.JobMatchResponse;
import com.cvanalyzer.repository.JobRepository;
import com.cvanalyzer.repository.ResumeRepository;
import com.cvanalyzer.repository.ResumeSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ResumeSkillRepository resumeSkillRepository;

    @Autowired
    private SkillExtractionService skillExtractionService;

    public JobDescription saveJob(JobDescription job) {
        return jobRepository.save(job);
    }

    public List<JobDescription> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<JobDescription> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public boolean deleteJob(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public JobMatchResponse calculateMatch(Long jobId, Long resumeId) {
        Optional<JobDescription> job = jobRepository.findById(jobId);
        Optional<Resume> resume = resumeRepository.findById(resumeId);

        if (job.isEmpty() || resume.isEmpty()) {
            return new JobMatchResponse(resumeId, jobId, 0.0);
        }

        // Get resume skills
        List<ResumeSkill> resumeSkills = resumeSkillRepository.findByResumeId(resumeId);
        List<String> resumeSkillNames = resumeSkills.stream()
                .map(rs -> rs.getSkill().getName().toLowerCase())
                .collect(Collectors.toList());

        // Extract job skills from description and requirements
        String jobText = job.get().getDescription() + " " + job.get().getRequirements();
        List<String> jobSkillNames = extractSkillsFromJobText(jobText);

        // Calculate match
        List<String> matchedSkills = resumeSkillNames.stream()
                .filter(skill -> jobSkillNames.contains(skill))
                .collect(Collectors.toList());

        List<String> missingSkills = jobSkillNames.stream()
                .filter(skill -> !resumeSkillNames.contains(skill))
                .collect(Collectors.toList());

        double matchPercentage = jobSkillNames.isEmpty() ? 0.0 :
                (double) matchedSkills.size() / jobSkillNames.size() * 100;

        JobMatchResponse response = new JobMatchResponse(resumeId, jobId, matchPercentage);
        response.setMatchedSkills(matchedSkills);
        response.setMissingSkills(missingSkills);

        // Set match level
        if (matchPercentage >= 80) response.setMatchLevel("Excellent");
        else if (matchPercentage >= 60) response.setMatchLevel("Good");
        else if (matchPercentage >= 40) response.setMatchLevel("Fair");
        else response.setMatchLevel("Poor");

        return response;
    }

    private List<String> extractSkillsFromJobText(String text) {
        // Simple skill extraction - you can enhance this with OpenNLP
        String[] commonSkills = {
                "java", "python", "javascript", "react", "spring", "mysql", "html", "css",
                "nodejs", "angular", "vue", "docker", "kubernetes", "aws", "git", "jenkins",
                "mongodb", "postgresql", "redis", "elasticsearch", "microservices", "restapi",
                "graphql", "typescript", "php", "laravel", "django", "flask", "express"
        };

        String lowerText = text.toLowerCase();
        return Arrays.stream(commonSkills)
                .filter(skill -> lowerText.contains(skill))
                .collect(Collectors.toList());
    }
}
