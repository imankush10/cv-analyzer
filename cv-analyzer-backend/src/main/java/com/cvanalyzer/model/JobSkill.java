package com.cvanalyzer.model;

import jakarta.persistence.*;

@Entity
@Table(name = "job_skills")
public class JobSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobDescription jobDescription;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column(name = "importance_level")
    private Integer importanceLevel; // 1-5 scale

    @Column(name = "is_required")
    private Boolean isRequired;

    // Constructors, getters, and setters
    public JobSkill() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public JobDescription getJobDescription() { return jobDescription; }
    public void setJobDescription(JobDescription jobDescription) { this.jobDescription = jobDescription; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public Integer getImportanceLevel() { return importanceLevel; }
    public void setImportanceLevel(Integer importanceLevel) { this.importanceLevel = importanceLevel; }

    public Boolean getIsRequired() { return isRequired; }
    public void setIsRequired(Boolean isRequired) { this.isRequired = isRequired; }
}
