package com.example.speech.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhisperResult {
    private String filename;
    private String transcript;
}
