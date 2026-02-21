package com.example.ManualJobTracker.repository;

import com.example.ManualJobTracker.model.JobTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTrackerRepo extends JpaRepository<JobTracker ,Integer > {
    void deleteByStatus(String status);
    boolean existsByMessageId(String messageId);

}
