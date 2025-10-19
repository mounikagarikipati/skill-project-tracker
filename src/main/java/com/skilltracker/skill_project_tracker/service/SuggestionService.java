package com.skilltracker.skill_project_tracker.service;

import com.skilltracker.skill_project_tracker.dto.SuggestionDto;
import com.skilltracker.skill_project_tracker.model.Skill;
import com.skilltracker.skill_project_tracker.repo.ProjectRepository;
import com.skilltracker.skill_project_tracker.repo.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuggestionService {

    private final SkillRepository skillRepo;
    private final ProjectRepository projectRepo;

    public SuggestionService(SkillRepository skillRepo, ProjectRepository projectRepo) {
        this.skillRepo = skillRepo;
        this.projectRepo = projectRepo;
    }

    public List<SuggestionDto> suggestionsForUser(Long userId) {
        var suggestions = new ArrayList<SuggestionDto>();
        var skills = skillRepo.findByUser_IdOrderByCreatedAtDesc(userId);
        var projects = projectRepo.findByUser_IdOrderByCreatedAtDesc(userId);

        boolean hasJava = hasSkill(skills, "java");
        boolean hasSpring = containsInSkill(skills, "spring");
        boolean hasSql = containsInSkill(skills, "sql");
        long completed = projects.stream()
                .filter(p -> p.getStatus() != null && p.getStatus().equalsIgnoreCase("Completed"))
                .count();

        // Next-skill nudges
        if (hasJava && !hasSpring) {
            suggestions.add(new SuggestionDto(
                    "skill",
                    "Learn Spring Boot Basics",
                    "You have Java logged. Spring Boot is a natural next step for building services.",
                    "https://spring.io/guides"
            ));
        }
        if (hasSpring && !hasSql) {
            suggestions.add(new SuggestionDto(
                    "skill",
                    "Add SQL + JPA",
                    "Persistence unlocks CRUD apps with real data in Spring.",
                    "https://spring.io/guides/gs/accessing-data-jpa/"
            ));
        }
        if (hasSpring && hasSql) {
            suggestions.add(new SuggestionDto(
                    "project",
                    "Build a small CRUD app with auth",
                    "Your stack fits a simple login + add/list app. Ship it end-to-end.",
                    null
            ));
        }

        // Practice reps for beginner-level skills
        skills.stream()
                .filter(s -> s.getLevel() != null && s.getLevel().equalsIgnoreCase("Beginner"))
                .limit(2)
                .forEach(s -> suggestions.add(new SuggestionDto(
                        "resource",
                        "Practice: 3 quick exercises in " + s.getName(),
                        "Short reps will move you toward Intermediate.",
                        null
                )));

        // Momentum nudge
        if (completed == 0 && !projects.isEmpty()) {
            suggestions.add(new SuggestionDto(
                    "project",
                    "Finish one in-progress project",
                    "Closing a project boosts confidence and clarifies next steps.",
                    null
            ));
        }

        if (suggestions.isEmpty()) {
            suggestions.add(new SuggestionDto(
                    "project",
                    "Tiny weekend build",
                    "Pick any small idea you can finish in 4–6 hours to build momentum.",
                    null
            ));
        }
        return suggestions;
    }

    private boolean hasSkill(List<Skill> skills, String exact) {
        return skills.stream().anyMatch(s -> s.getName() != null && s.getName().equalsIgnoreCase(exact));
    }
    private boolean containsInSkill(List<Skill> skills, String fragment) {
        return skills.stream().anyMatch(s ->
                s.getName() != null && s.getName().toLowerCase().contains(fragment.toLowerCase()));
    }
}
