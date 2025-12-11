package com.skilltracker.skill_project_tracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.List;
import java.util.Map;

@Service
public class GroqService {

    private static final Logger log = LoggerFactory.getLogger(GroqService.class);

    private final WebClient groqClient;
    private final String model = "llama-3.1-8b-instant";

    public GroqService(WebClient groqClient) {
        this.groqClient = groqClient;
    }

    public String getSkillSuggestions(String skillName, String level, String notes) {
        String userPrompt = """
                Give 3 specific next steps to improve the skill: %s.
                Level: %s.
                Notes: %s.
                Keep the response short and actionable.
                """.formatted(skillName, level, notes);

        try {
            Map<String, Object> body = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content", "You are a helpful learning coach."),
                            Map.of("role", "user", "content", userPrompt)
                    )
            );

            Map<String, Object> response = groqClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return extractContent(response);
        }
        catch (WebClientResponseException e) {
            log.error("Groq error: {}", e.getResponseBodyAsString());
            return "AI suggestions are temporarily unavailable.";
        }
        catch (Exception e) {
            log.error("Unexpected Groq error", e);
            return "AI suggestions failed due to an internal error.";
        }
    }

    public String chatAboutSkill(String skillName, String message) {
        try {
            Map<String, Object> body = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content", "You are a senior instructor for: " + skillName),
                            Map.of("role", "user", "content", message)
                    )
            );

            Map<String, Object> response = groqClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return extractContent(response);
        }
        catch (WebClientResponseException e) {
            log.error("Groq error: {}", e.getResponseBodyAsString());
            return "AI chat is unavailable right now.";
        }
        catch (Exception e) {
            log.error("Unexpected Groq error", e);
            return "AI chat failed due to an internal error.";
        }
    }

    private String extractContent(Map<String, Object> response) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return message.get("content").toString();
        } catch (Exception e) {
            return "AI returned an unexpected response format.";
        }
    }
}
