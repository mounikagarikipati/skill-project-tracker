package com.skilltracker.skill_project_tracker.dto;

import java.time.Instant;

public class ProjectResponse {
    public Long id;
    public String title;
    public String description;
    public String techStack;
    public String status;
    public Instant createdAt;

    public ProjectResponse(Long id, String title, String description, String techStack, String status, Instant createdAt) {
        this.id = id; this.title = title; this.description = description;
        this.techStack = techStack; this.status = status; this.createdAt = createdAt;
    }
}

