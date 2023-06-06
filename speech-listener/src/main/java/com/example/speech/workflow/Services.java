package com.example.speech.workflow;

public enum Services{

    STT{
        @Override
        public String toString() {
            return "speech-to-text";
        }

        @Override
        public String errorMessage(){
            return STT_ERROR_MESSAGE;
        }
    },
    GPT{
        @Override
        public String toString() {
            return "gpt-api";
        }

        @Override
        public String errorMessage(){
            return GPT_ERROR_MESSAGE;
        }
    },
    TTS{
        @Override
        public String toString() {
            return "text-to-speech";
        }

        @Override
        public String errorMessage(){
            return TTS_ERROR_MESSAGE;
        }
    };

    private static final String STT_ERROR_MESSAGE = "Something is wrong with speech-to-text module, so can't get speech transcript! " +
            "Check stacktrace for more information";
    private static final String GPT_ERROR_MESSAGE = "Something is wrong with gpt module, so can't get response of gpt model! " +
            "Check stacktrace for more information";
    private static final String TTS_ERROR_MESSAGE = "Something is wrong with text-to-speech module, so can't play audio message! " +
            "Check stacktrace for more information";

    public String errorMessage(){
        return "---";
    }
}