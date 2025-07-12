package com.cvanalyzer.repository;

import com.cvanalyzer.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByIsActiveTrue();
    List<Skill> findByCategory(String category);
    List<Skill> findByNameContainingIgnoreCase(String name);
    Optional<Skill> findByNameIgnoreCase(String name); // <-- Add this
    @Query("SELECT s FROM Skill s WHERE s.isActive = true AND " +
            "(LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.synonyms) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Skill> findByNameOrSynonyms(@Param("keyword") String keyword);
}

