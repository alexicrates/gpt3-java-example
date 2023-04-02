package com.example.gptapi.model.entities;

import jakarta.persistence.*;
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

    @Column(length = 2000)
    private String prompt;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    public enum MessageType {
        INPUT,
        OUTPUT
    }
}

