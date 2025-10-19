package com.skilltracker.skill_project_tracker.dto;

public record SuggestionDto(
        String type,    // "project" | "skill" | "resource"
        String title,
        String reason,
        String link     // optional
) {}

