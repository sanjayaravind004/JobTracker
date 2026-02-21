package com.example.ManualJobTracker.service;

import java.time.LocalDate;
import java.util.Properties;

import com.example.ManualJobTracker.model.JobTracker;
import com.example.ManualJobTracker.repository.JobTrackerRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailReaderService {

    @Autowired
    JobTrackerRepo jobTrackerRepo;

    @Autowired
    AIExtractorService aiExtractorService;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    private final ObjectMapper mapper = new ObjectMapper();

    public void readEmails() {

        try {

            // ⭐ Gmail IMAP settings
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);

            // ⭐ Connect Gmail
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", username, password);

            // ⭐ Open Inbox
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();

            System.out.println("Total Emails: " + messages.length);

            // ⭐ Read last 5 emails
            for (int i = messages.length - 1; i >= Math.max(0, messages.length - 5); i--) {

                String subject = messages[i].getSubject();
                System.out.println("Subject: " + subject);

                String[] header = messages[i].getHeader("Message-ID");
                String messageId = (header != null && header.length > 0) ? header[0] : null;

                System.out.println("Message ID = " + messageId);

                // ⭐ Only process possible job emails
                if(subject != null) {

                    String lower = subject.toLowerCase();

                    if(lower.contains("application") ||
                            lower.contains("position") ||
                            lower.contains("job") ||
                            lower.contains("role") ||
                            lower.contains("interview") ||
                            lower.contains("java")) {

                        // ⭐ Skip duplicates
                        if(messageId != null && jobTrackerRepo.existsByMessageId(messageId)){
                            System.out.println("Duplicate email skipped!");
                            continue;
                        }

                        // ⭐ Call Mistral AI
                        String emailText = "Subject: " + subject;
                        String aiResponse = aiExtractorService.extractJobInfo(emailText);
                        System.out.println("AI RESPONSE = " + aiResponse);

                        String company = "Unknown";
                        String role = "Unknown";
                        String status = "Applied";

                        try {

                            // ⭐ Remove markdown formatting if exists
                            aiResponse = aiResponse.replace("```json","")
                                    .replace("```","")
                                    .trim();

                            // ⭐ Parse JSON safely
                            JsonNode node = mapper.readTree(aiResponse);

                            company = node.path("company").asText("Unknown");
                            role = node.path("role").asText("Unknown");
                            status = node.path("status").asText("Applied");
                            if(status.equalsIgnoreCase("onsite") ||
                                    status.equalsIgnoreCase("remote") ||
                                    status.equalsIgnoreCase("hybrid") ||
                                    status.equalsIgnoreCase("received") ||
                                    status.equalsIgnoreCase("submitted") ||
                                    status.equalsIgnoreCase("processing")) {

                                status = "Applied";
                            }

                        } catch (Exception e){
                            System.out.println("AI JSON parsing failed");
                        }

                        // ⭐ SAFE FALLBACK
                        if(company == null || company.isBlank()) company = "Unknown";
                        if(role == null || role.isBlank()) role = "Unknown";
                        if(status == null || status.isBlank()) status = "Applied";

                        // ⭐ Save to DB
                        JobTracker job = new JobTracker();
                        job.setCompanyName(company);
                        job.setRole(role);
                        job.setStatus(status);
                        job.setAppliedDate(LocalDate.now());
                        job.setNotes(subject);
                        job.setMessageId(messageId);

                        jobTrackerRepo.save(job);

                        System.out.println("Auto Job Created from Email!");

                        // ⭐ Rate-limit protection for Mistral
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }

            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}