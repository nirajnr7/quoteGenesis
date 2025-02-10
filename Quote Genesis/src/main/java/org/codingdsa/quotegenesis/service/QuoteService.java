package org.codingdsa.quotegenesis.service;

import org.codingdsa.quotegenesis.dto.QuoteRequestDTO;
import org.codingdsa.quotegenesis.dto.QuoteResponseDTO;
import org.codingdsa.quotegenesis.model.FamousPerson;
import org.codingdsa.quotegenesis.model.GeneratedQuote;
import org.codingdsa.quotegenesis.model.QuoteType;
import org.codingdsa.quotegenesis.repository.FamousPersonRepository;
import org.codingdsa.quotegenesis.repository.GeneratedQuoteRepository;
import org.codingdsa.quotegenesis.repository.QuoteTypeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Service
public class QuoteService {

    private final RestTemplate restTemplate;
    private final QuoteTypeRepository quoteTypeRepository;
    private final FamousPersonRepository famousPersonRepository;
    private final GeneratedQuoteRepository generatedQuoteRepository;
    private final Random random = new Random();

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    public QuoteService(RestTemplate restTemplate, QuoteTypeRepository quoteTypeRepository,
                        FamousPersonRepository famousPersonRepository, GeneratedQuoteRepository generatedQuoteRepository) {
        this.restTemplate = restTemplate;
        this.quoteTypeRepository = quoteTypeRepository;
        this.famousPersonRepository = famousPersonRepository;
        this.generatedQuoteRepository = generatedQuoteRepository;
    }

    public String fetchQuote() {
        // Fetch all quote types and famous people from DB
        List<QuoteType> quoteTypes = quoteTypeRepository.findAll();
        List<FamousPerson> famousPeople = famousPersonRepository.findAll();

        if (quoteTypes.isEmpty() || famousPeople.isEmpty()) {
            return "Database is empty. Please insert quote types and famous people.";
        }

        // Pick random type and person
        String randomType = quoteTypes.get(random.nextInt(quoteTypes.size())).getType();
        String randomPerson = famousPeople.get(random.nextInt(famousPeople.size())).getName();

        String prompt = String.format(
                "Generate a %s quote attributed to %s. " +
                        "The response should strictly follow this format:\n\n" +
                        "\"<quote>\" - %s\n\n" +
                        "Example:\n" +
                        "\"Success is not final, failure is not fatal: it is the courage to continue that counts.\" - Winston Churchill",
                randomType, randomPerson, randomPerson
        );


        // Create request body
        QuoteRequestDTO requestDTO = new QuoteRequestDTO(
                "llama-3.3-70b-versatile",
                List.of(new QuoteRequestDTO.Message("user", prompt))
        );

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<QuoteRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

        // Call API
        ResponseEntity<QuoteResponseDTO> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                QuoteResponseDTO.class
        );

        // Extract response
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            String generatedQuote = response.getBody().getChoices().get(0).getMessage().getContent();

            // Save to database
            GeneratedQuote savedQuote = new GeneratedQuote();
            savedQuote.setQuoteText(generatedQuote);
            savedQuote.setAuthor(randomPerson);
            savedQuote.setType(randomType);
            generatedQuoteRepository.save(savedQuote);

            return generatedQuote;
        } else {
            return "Failed to fetch quote!";
        }
    }
}
