package org.codingdsa.quotegenesis.controller;

import org.codingdsa.quotegenesis.service.QuoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/generate")
    public ResponseEntity<String> generateQuote() {
        String quote = quoteService.fetchQuote();
        return ResponseEntity.ok(quote);
    }
}
