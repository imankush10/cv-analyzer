package com.cvanalyzer.repository;

import com.cvanalyzer.model.ResumeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeSkillRepository extends JpaRepository<ResumeSkill, Long> {
    List<ResumeSkill> findByResumeId(Long resumeId);
    List<ResumeSkill> findBySkillId(Long skillId);
    void deleteByResumeId(Long resumeId);
}
