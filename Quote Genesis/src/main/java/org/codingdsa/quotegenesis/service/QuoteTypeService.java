package org.codingdsa.quotegenesis.service;

import org.codingdsa.quotegenesis.dto.QuoteTypeDto;
import org.codingdsa.quotegenesis.model.QuoteType;
import org.codingdsa.quotegenesis.repository.QuoteTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuoteTypeService {
    @Autowired
    private QuoteTypeRepository quoteTypeRepository;

    public QuoteType addQuoteType(QuoteTypeDto quoteTypeDto) {
        QuoteType quoteType = new QuoteType();
        // Set properties from quoteTypeDto to quoteType
        quoteType.setType(quoteTypeDto.getType());
        // Add other properties as needed
        return quoteTypeRepository.save(quoteType);
    }
}
