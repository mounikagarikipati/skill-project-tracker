package com.skilltracker.skill_project_tracker.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
    // Landing page after login
    @GetMapping({"/", "/home"})
    public String home() {
        return "app";

    }

}
