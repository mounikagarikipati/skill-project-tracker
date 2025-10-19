package com.skilltracker.skill_project_tracker.repo;

import com.skilltracker.skill_project_tracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser_IdOrderByCreatedAtDesc(Long userId);
}
