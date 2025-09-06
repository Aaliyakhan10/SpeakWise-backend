package com.doftec.SpeakWise.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiResponse {
    private String sentiment;
    private String emotion;
    private String intent;
    private List<String> topics;
    private String summary;
    private List<GrammarMistake> grammarMistakes;
    private List<String> fillerWords;
    private String speechPaceEstimate;
    private Clarity clarity;

    @Data
    public static class GrammarMistake {
        private String error;
        private String suggestion;
    }

    @Data
    public static class Clarity {
        private boolean clear;
        private String reason;
    }
}
