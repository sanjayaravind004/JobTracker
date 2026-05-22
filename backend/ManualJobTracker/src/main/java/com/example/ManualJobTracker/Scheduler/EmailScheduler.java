package com.example.ManualJobTracker.Scheduler;

import com.example.ManualJobTracker.service.EmailReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@Component
@EnableScheduling
public class EmailScheduler {

    @Autowired
    EmailReaderService emailReaderService;

    @Scheduled(fixedRate = 600000) //every 10 mins
    public void runEmailReader(){
        emailReaderService.readEmails();
    }
}
