package com.example.gptlogsspringbootstarter.model.repositories;

import com.example.gptlogsspringbootstarter.model.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostgresRepository extends JpaRepository<ChatMessage, UUID> {}
