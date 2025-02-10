package org.codingdsa.quotegenesis.controller;

import org.codingdsa.quotegenesis.dto.FamousPersonDto;
import org.codingdsa.quotegenesis.service.FamousPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/famous-person")
public class FamousPersonController {
    @Autowired
    private FamousPersonService famousPersonService;

    @PostMapping
    public ResponseEntity<?> addFamousPerson(@RequestBody FamousPersonDto famousPersonDto) {
        return ResponseEntity.ok(famousPersonService.addFamousPerson(famousPersonDto));
    }
}
