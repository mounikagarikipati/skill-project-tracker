package com.skilltracker.skill_project_tracker.dto;

public class SkillCreateRequest {
    public Long userId;   // MVP: we pass userId from client; later replace with JWT/session
    public String name;
    public String level;  // optional
    public String notes;  // optional
}
