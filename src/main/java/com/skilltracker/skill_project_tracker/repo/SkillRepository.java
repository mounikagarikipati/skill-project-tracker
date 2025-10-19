package com.skilltracker.skill_project_tracker.repo;

import com.skilltracker.skill_project_tracker.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByUser_IdOrderByCreatedAtDesc(Long userId);
}
