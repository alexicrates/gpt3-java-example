package com.example.model.repositories;

import com.example.model.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostgresRepository extends JpaRepository<ChatMessage, UUID> {}