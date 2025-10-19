package com.skilltracker.skill_project_tracker.controller;

import com.skilltracker.skill_project_tracker.dto.SkillCreateRequest;
import com.skilltracker.skill_project_tracker.dto.SkillResponse;
import com.skilltracker.skill_project_tracker.model.Skill;
import com.skilltracker.skill_project_tracker.model.User;
import com.skilltracker.skill_project_tracker.repo.SkillRepository;
import com.skilltracker.skill_project_tracker.repo.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "*")
public class SkillController {

    private final SkillRepository skillRepo;
    private final UserRepository userRepo;

    public SkillController(SkillRepository skillRepo, UserRepository userRepo) {
        this.skillRepo = skillRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public ResponseEntity<?> listByUser(@RequestParam Long userId) {
        if (userId == null) return ResponseEntity.badRequest().body(Map.of("error", "userId required"));
        List<SkillResponse> out = skillRepo.findByUser_IdOrderByCreatedAtDesc(userId).stream()
                .map(s -> new SkillResponse(s.getId(), s.getName(), s.getLevel(), s.getNotes(), s.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(out);
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody SkillCreateRequest req) {
        if (req.userId == null || req.name == null || req.name.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "userId and name are required"));
        }
        User u = userRepo.findById(req.userId).orElse(null);
        if (u == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "invalid userId"));
        }

        Skill s = new Skill();
        s.setUser(u);
        s.setName(req.name);
        s.setLevel(req.level);
        s.setNotes(req.notes);

        Skill saved = skillRepo.save(s);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long userId) {
        return skillRepo.findById(id).map(s -> {
            if (!s.getUser().getId().equals(userId)) {
                return ResponseEntity.status(403).body(Map.of("error", "forbidden"));
            }
            skillRepo.delete(s);
            return ResponseEntity.ok(Map.of("message", "deleted"));
        }).orElse(ResponseEntity.notFound().build());
    }
}
