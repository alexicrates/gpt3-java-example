package com.example.gpt3javaexample.model.repositories;

import com.example.gpt3javaexample.model.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostgresRepository extends JpaRepository<ChatMessage, UUID> {}
