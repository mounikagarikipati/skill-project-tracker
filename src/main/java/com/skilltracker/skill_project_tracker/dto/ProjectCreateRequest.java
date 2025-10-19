package com.skilltracker.skill_project_tracker.dto;

public class ProjectCreateRequest {
    public Long userId;          // MVP
    public String title;
    public String description;   // optional
    public String techStack;     // optional
    public String status;        // optional ("In Progress"/"Completed")
}
