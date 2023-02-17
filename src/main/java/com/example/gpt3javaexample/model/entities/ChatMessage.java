package com.example.gpt3javaexample.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class ChatMessage {

    public ChatMessage(String prompt, MessageType messageType) {
        this.prompt = prompt;
        this.messageType = messageType;
        this.createdAt = Timestamp.from(Instant.now());
        this.id = UUID.randomUUID();
    }

    @Id
    private UUID id;

    private Timestamp createdAt;

    private String prompt;

    private MessageType messageType;
}

