package com.example.speech.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WhisperResponse {
    List<WhisperResult> results;
}
