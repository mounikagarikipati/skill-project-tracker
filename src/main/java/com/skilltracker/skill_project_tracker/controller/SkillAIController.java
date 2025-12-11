package com.skilltracker.skill_project_tracker.controller;

import com.skilltracker.skill_project_tracker.dto.SkillChatRequest;
import com.skilltracker.skill_project_tracker.dto.SkillChatResponse;
import com.skilltracker.skill_project_tracker.dto.SkillSuggestionResponse;
import com.skilltracker.skill_project_tracker.model.Skill;
import com.skilltracker.skill_project_tracker.repo.SkillRepository;
import com.skilltracker.skill_project_tracker.service.GroqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillAIController {

    private final SkillRepository skillRepository;
    private final GroqService groqService;

    // ----- DTO for update -----
    public static class SkillUpdateRequest {
        private String level;
        private String notes;

        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    // ----- Get one skill (used by modal) -----
    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkill(@PathVariable Long id) {
        return skillRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ----- Update level + notes only, return NO CONTENT -----
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSkill(
            @PathVariable Long id,
            @RequestBody SkillUpdateRequest request
    ) {
        return skillRepository.findById(id)
                .map(existing -> {
                    existing.setLevel(request.getLevel());
                    existing.setNotes(request.getNotes());
                    skillRepository.save(existing);
                    return ResponseEntity.noContent().build(); // 204, no body
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // ----- AI suggestions -----
    @PostMapping("/{id}/ai-suggestions")
    public ResponseEntity<SkillSuggestionResponse> getSkillSuggestions(@PathVariable Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        String suggestions = groqService.getSkillSuggestions(
                skill.getName(),
                skill.getLevel(),
                skill.getNotes()
        );

        return ResponseEntity.ok(new SkillSuggestionResponse(suggestions));
    }

    // ----- AI chat -----
    @PostMapping("/{id}/ai-chat")
    public ResponseEntity<SkillChatResponse> chatAboutSkill(
            @PathVariable Long id,
            @RequestBody SkillChatRequest req
    ) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        String reply = groqService.chatAboutSkill(skill.getName(), req.getMessage());
        return ResponseEntity.ok(new SkillChatResponse(reply));
    }
}
