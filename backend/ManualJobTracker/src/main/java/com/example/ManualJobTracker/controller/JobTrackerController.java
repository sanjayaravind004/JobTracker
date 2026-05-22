package com.example.ManualJobTracker.controller;

import com.example.ManualJobTracker.model.JobTracker;
import com.example.ManualJobTracker.service.JobTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins ={"http://localhost:5173" , 
		"https://jobtracker-1-bzq6.onrender.com"})
@RestController
public class JobTrackerController {
    @Autowired
    JobTrackerService jobTrackerService;

    @GetMapping("/job/all")
    public List<JobTracker> getAllJobs(){
        return jobTrackerService.getAllJobs();
    }

    @PostMapping("/job/add")
    public JobTracker addNewJob(@RequestBody JobTracker jobTracker){
         jobTrackerService.addNewJob(jobTracker);
         return jobTracker;
    }

    @PutMapping("/job/update/{id}")
    public JobTracker updateJob(@PathVariable("id") int id , @RequestBody JobTracker jobTracker) {
        jobTracker.setId(id);
        return jobTrackerService.updateJobById(jobTracker);
    }

    @PutMapping("/job/update")
    public JobTracker updateJobs(@RequestBody JobTracker jobTracker){
        return jobTrackerService.updateJobs(jobTracker);
    }

    @DeleteMapping("/job/clear/{status}")
    public String clearByStatus(@PathVariable("status") String status){
        //jobTracker.setStatus(status);
         jobTrackerService.clearByStatus(status);
         return "Data Cleared Succesfully";
        }



}
