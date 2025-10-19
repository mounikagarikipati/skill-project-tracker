package com.skilltracker.skill_project_tracker.dto;

import java.time.Instant;

public class SkillResponse {
    public Long id;
    public String name;
    public String level;
    public String notes;
    public Instant createdAt;

    public SkillResponse(Long id, String name, String level, String notes, Instant createdAt) {
        this.id = id; this.name = name; this.level = level; this.notes = notes; this.createdAt = createdAt;
    }
}
