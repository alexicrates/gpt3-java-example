package com.example.gpt3javaexample.aop;

import com.example.gpt3javaexample.model.entities.ChatMessage;
import com.example.gpt3javaexample.model.repositories.PostgresRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.gpt3javaexample.model.entities.ChatMessage.MessageType.INPUT;
import static com.example.gpt3javaexample.model.entities.ChatMessage.MessageType.OUTPUT;

@Aspect
@Component
public class LogAspect {

    private final PostgresRepository postgresRepository;

    @Autowired
    public LogAspect(PostgresRepository postgresRepository) {
        this.postgresRepository = postgresRepository;
    }

    @Before(value = "@annotation(SaveToLogs)")
    public void saveInputToLogs(JoinPoint joinPoint){
        String prompt = (String) joinPoint.getArgs()[0];

        postgresRepository.save(new ChatMessage(prompt, INPUT));

        System.out.println(prompt);
    }
    
    @AfterReturning(pointcut = "@annotation(SaveToLogs)", returning = "prompt")
    public void saveOutputToLogs(String prompt){

        postgresRepository.save(new ChatMessage(prompt, OUTPUT));

        System.out.println(prompt);
    }
}


