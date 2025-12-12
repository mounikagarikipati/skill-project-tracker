package com.skilltracker.skill_project_tracker.repo;

import com.skilltracker.skill_project_tracker.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByIdAndUser_Id(Long id, Long userId);
    List<Skill> findByUser_IdOrderByCreatedAtDesc(Long userId);
}
