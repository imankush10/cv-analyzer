package com.cvanalyzer.repository;

import com.cvanalyzer.model.JobSkill;
import com.cvanalyzer.model.JobDescription;
import com.cvanalyzer.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobSkillRepository extends JpaRepository<JobSkill, Long> {

    // Find all skills for a specific job
    List<JobSkill> findByJobDescription(JobDescription jobDescription);

    // Find all jobs that require a specific skill
    List<JobSkill> findBySkill(Skill skill);

    // Find required skills for a job
    List<JobSkill> findByJobDescriptionAndIsRequiredTrue(JobDescription jobDescription);

    // Find skills by importance level
    List<JobSkill> findByImportanceLevel(Integer importanceLevel);

    // Find skills for a job with minimum importance level
    @Query("SELECT js FROM JobSkill js WHERE js.jobDescription = :job AND js.importanceLevel >= :minLevel")
    List<JobSkill> findByJobAndMinImportanceLevel(@Param("job") JobDescription job, @Param("minLevel") Integer minLevel);

    // Check if a specific skill exists for a job
    Optional<JobSkill> findByJobDescriptionAndSkill(JobDescription jobDescription, Skill skill);

    // Find all required skills across all jobs
    List<JobSkill> findByIsRequiredTrue();

    // Count skills for a specific job
    long countByJobDescription(JobDescription jobDescription);

    // Delete all skills for a specific job
    void deleteByJobDescription(JobDescription jobDescription);

    // Find skills by job ID
    @Query("SELECT js FROM JobSkill js WHERE js.jobDescription.id = :jobId")
    List<JobSkill> findByJobDescriptionId(@Param("jobId") Long jobId);
}
