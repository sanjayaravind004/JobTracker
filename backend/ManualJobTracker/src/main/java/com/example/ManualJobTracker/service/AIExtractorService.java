package com.example.ManualJobTracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class AIExtractorService {

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.url}")
    private String apiUrl;

    private final ObjectMapper mapper = new ObjectMapper();

    private final WebClient webClient;

    public AIExtractorService(@Value("${ai.api.url}") String url,
                              @Value("${ai.api.key}") String key) {

        this.webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", "Bearer " + key)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String extractJobInfo(String emailText){

        try {

            String prompt = """
                    Extract company name, job role and status from this email.
                    Return ONLY JSON:
                    {"company":"","role":"","status":""}

                    Email:
                    """ + emailText;

            Map<String,Object> body = Map.of(
                    "model","mistral-small-latest",
                    "messages", List.of(
                            Map.of("role","user","content",prompt)
                    )
            );

            String response = webClient.post()
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("RAW MISTRAL RESPONSE = " + response);

            JsonNode root = mapper.readTree(response);

            String aiText =
                    root.path("choices")
                            .get(0)
                            .path("message")
                            .path("content")
                            .asText();

            System.out.println("PARSED AI TEXT = " + aiText);

            return aiText;

        } catch (Exception e){
            e.printStackTrace();
            return "{\"company\":\"Unknown\",\"role\":\"Unknown\",\"status\":\"Applied\"}";
        }
    }
}