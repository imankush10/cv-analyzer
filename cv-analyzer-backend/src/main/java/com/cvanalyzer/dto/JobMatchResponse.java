package com.cvanalyzer.dto;

import java.util.List;

public class JobMatchResponse {
    private Long resumeId;
    private Long jobId;
    private Double matchPercentage;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String matchLevel;

    public JobMatchResponse() {}

    public JobMatchResponse(Long resumeId, Long jobId, Double matchPercentage) {
        this.resumeId = resumeId;
        this.jobId = jobId;
        this.matchPercentage = matchPercentage;
    }

    // Getters and setters
    public Long getResumeId() { return resumeId; }
    public void setResumeId(Long resumeId) { this.resumeId = resumeId; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public Double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(Double matchPercentage) { this.matchPercentage = matchPercentage; }

    public List<String> getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; }

    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }

    public String getMatchLevel() { return matchLevel; }
    public void setMatchLevel(String matchLevel) { this.matchLevel = matchLevel; }
}
