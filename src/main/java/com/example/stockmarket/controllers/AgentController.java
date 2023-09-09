package com.example.stockmarket.controllers;

import com.example.stockmarket.entities.Agent;
import com.example.stockmarket.services.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AgentController {

    private final AgentService agentService;

    @Autowired
    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/agents")
    public String listAgents(Model model) {
        model.addAttribute("agents", agentService.getAllAgents());
        return "agents"; // Return the name of the Thymeleaf template (e.g., "agents.html")
    }

    // Add more methods for handling agent-related requests
}
