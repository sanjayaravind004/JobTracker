package com.example.ManualJobTracker.service;

import com.example.ManualJobTracker.model.JobTracker;
import com.example.ManualJobTracker.repository.JobTrackerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class JobTrackerService {
    @Autowired
    JobTrackerRepo jobTrackerRepo;


    public List<JobTracker> getAllJobs() {
        return jobTrackerRepo.findAll();
    }

    public void addNewJob(JobTracker jobTracker) {
        if(jobTracker.getStatus() == null || jobTracker.getStatus().isEmpty()){
            jobTracker.setStatus("Applied"); //use of getter and setter
        }
        if(jobTracker.getAppliedDate()==null){
            jobTracker.setAppliedDate(LocalDate.now());
        }
         jobTrackerRepo.save(jobTracker);
    }

    public JobTracker updateJobById(JobTracker jobTracker) {
        jobTrackerRepo.save(jobTracker);
        return jobTracker;
    }

    public JobTracker updateJobs(JobTracker jobTracker){
        if(jobTracker.getAppliedDate() != null && jobTracker.getAppliedDate().isBefore(LocalDate.now().minusDays(10)) && jobTracker.getStatus().equalsIgnoreCase("Applied") ){
            jobTracker.setStatus("No Response");
        }
     jobTrackerRepo.save(jobTracker);
     return jobTracker;
    }

    @Transactional
    public void  clearByStatus(String status) {
        jobTrackerRepo.deleteByStatus(status.trim());
    }
    @Scheduled(cron = "0 0 0 * * *")//0   → second 0   → minute 0   → hour (midnight) */10 → every 10th day *   → every month *   → any weekday
    public void  clearByStatus() {
        clearByStatus("rejected");
    }
}
