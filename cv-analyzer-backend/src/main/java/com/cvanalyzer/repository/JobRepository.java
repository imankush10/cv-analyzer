package com.cvanalyzer.repository;

import com.cvanalyzer.model.JobDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<JobDescription, Long> {
}
