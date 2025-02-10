package org.codingdsa.quotegenesis.controller;

import org.codingdsa.quotegenesis.dto.QuoteTypeDto;
import org.codingdsa.quotegenesis.service.QuoteTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quote-type")
public class QuoteTypeController {
    @Autowired
    private QuoteTypeService quoteTypeService;

    @PostMapping
    public ResponseEntity<?> addQuoteType(@RequestBody QuoteTypeDto quoteTypeDto) {
        return ResponseEntity.ok(quoteTypeService.addQuoteType(quoteTypeDto));
    }
}
