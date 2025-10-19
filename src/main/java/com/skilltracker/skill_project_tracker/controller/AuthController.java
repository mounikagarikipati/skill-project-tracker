package com.skilltracker.skill_project_tracker.controller;

import com.skilltracker.skill_project_tracker.dto.LoginRequest;
import com.skilltracker.skill_project_tracker.dto.RegisterRequest;
import com.skilltracker.skill_project_tracker.model.User;
import com.skilltracker.skill_project_tracker.repo.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepo;

    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (req.username == null || req.username.isBlank() ||
                req.password == null || req.password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username and password are required"));
        }
        if (userRepo.existsByUsername(req.username)) {
            return ResponseEntity.badRequest().body(Map.of("error", "username already exists"));
        }
        if (req.email != null && !req.email.isBlank() && userRepo.existsByEmail(req.email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "email already in use"));
        }

        String hash = BCrypt.hashpw(req.password, BCrypt.gensalt());
        User u = new User();
        u.setUsername(req.username);
        u.setPasswordHash(hash);
        u.setEmail(req.email);
        userRepo.save(u);

        return ResponseEntity.ok(Map.of(
                "message", "user registered",
                "userId", u.getId(),
                "username", u.getUsername()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        if (req.username == null || req.password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "username and password are required"));
        }

        return userRepo.findByUsername(req.username)
                .map(u -> {
                    boolean ok = BCrypt.checkpw(req.password, u.getPasswordHash());
                    if (!ok) return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
                    return ResponseEntity.ok(Map.of(
                            "message", "login successful",
                            "userId", u.getId(),
                            "username", u.getUsername()
                    ));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error", "invalid credentials")));
    }
}
