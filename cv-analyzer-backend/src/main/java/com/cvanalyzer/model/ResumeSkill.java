package com.cvanalyzer.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "resume_skills")
public class ResumeSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    @JsonIgnore
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column(name = "proficiency_level")
    private Integer proficiencyLevel; // 1-5 scale based on context

    @Column(name = "match_confidence")
    private Double matchConfidence; // 0.0-1.0 confidence score

    @Column(name = "extracted_context")
    private String extractedContext; // Where/how skill was found

    // Constructors
    public ResumeSkill() {}

    public ResumeSkill(Resume resume, Skill skill, Integer proficiencyLevel, Double matchConfidence) {
        this.resume = resume;
        this.skill = skill;
        this.proficiencyLevel = proficiencyLevel;
        this.matchConfidence = matchConfidence;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Resume getResume() { return resume; }
    public void setResume(Resume resume) { this.resume = resume; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public Integer getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(Integer proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    public Double getMatchConfidence() { return matchConfidence; }
    public void setMatchConfidence(Double matchConfidence) { this.matchConfidence = matchConfidence; }

    public String getExtractedContext() { return extractedContext; }
    public void setExtractedContext(String extractedContext) { this.extractedContext = extractedContext; }
}
