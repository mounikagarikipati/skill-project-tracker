package com.skilltracker.skill_project_tracker.controller;

import com.skilltracker.skill_project_tracker.dto.ProjectCreateRequest;
import com.skilltracker.skill_project_tracker.dto.ProjectResponse;
import com.skilltracker.skill_project_tracker.model.Project;
import com.skilltracker.skill_project_tracker.model.User;
import com.skilltracker.skill_project_tracker.repo.ProjectRepository;
import com.skilltracker.skill_project_tracker.repo.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {
    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;

    public ProjectController(ProjectRepository projectRepo, UserRepository userRepo) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public ResponseEntity<?> listByUser(@RequestParam Long userId) {
        if (userId == null) return ResponseEntity.badRequest().body(Map.of("error", "userId required"));
        List<ProjectResponse> out = projectRepo.findByUser_IdOrderByCreatedAtDesc(userId).stream()
                .map(p -> new ProjectResponse(p.getId(), p.getTitle(), p.getDescription(), p.getTechStack(), p.getStatus(), p.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(out);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProjectCreateRequest req) {
        if (req.userId == null || req.title == null || req.title.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "userId and title are required"));
        }
        User u = userRepo.findById(req.userId).orElse(null);
        if (u == null) return ResponseEntity.badRequest().body(Map.of("error", "invalid userId"));

        Project p = new Project();
        p.setUser(u);
        p.setTitle(req.title);
        p.setDescription(req.description);
        p.setTechStack(req.techStack);
        if (req.status != null && !req.status.isBlank()) p.setStatus(req.status);

        return ResponseEntity.ok(projectRepo.save(p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long userId) {
        return projectRepo.findById(id).map(p -> {
            if (!p.getUser().getId().equals(userId)) {
                return ResponseEntity.status(403).body(Map.of("error", "forbidden"));
            }
            projectRepo.delete(p);
            return ResponseEntity.ok(Map.of("message", "deleted"));
        }).orElse(ResponseEntity.notFound().build());
    }
}
