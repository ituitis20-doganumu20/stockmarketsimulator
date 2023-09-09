package com.example.stockmarket.repositories;

import com.example.stockmarket.entities.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {
}
