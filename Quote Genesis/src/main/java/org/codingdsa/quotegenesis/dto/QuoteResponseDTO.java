package org.codingdsa.quotegenesis.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuoteResponseDTO {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
    }

    @Data
    public static class Message {
        private String content;
    }
}
