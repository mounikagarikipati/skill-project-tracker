package com.skilltracker.skill_project_tracker.controller;

import com.skilltracker.skill_project_tracker.dto.SuggestionDto;
import com.skilltracker.skill_project_tracker.service.SuggestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
@CrossOrigin(origins = "http://localhost:8080")
public class SuggestionController {

    private final SuggestionService service;

    public SuggestionController(SuggestionService service) {
        this.service = service;
    }

    @GetMapping
    public List<SuggestionDto> getSuggestions(@RequestParam Long userId) {
        return service.suggestionsForUser(userId);
    }
}
