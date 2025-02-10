package org.codingdsa.quotegenesis.scheduler;

import org.codingdsa.quotegenesis.service.QuoteService;
import org.codingdsa.quotegenesis.service.QuoteImageGenerator;
import org.codingdsa.quotegenesis.service.InstagramPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QuoteGenerationScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(QuoteGenerationScheduler.class);
    
    @Autowired
    private QuoteService quoteService;
    
    @Autowired
    private QuoteImageGenerator quoteImageGenerator;
    
    @Autowired
    private InstagramPostService instagramPostService;
    
    @Scheduled(cron = "${quote.generation.cron}")
    public void generateAndPostQuote() {
        try {
            logger.info("Starting scheduled quote generation and posting process");
            
            // Step 1: Generate new quote
            logger.info("Generating new quote");
            quoteService.fetchQuote();
            
            // Step 2: Generate image for the quote
            logger.info("Generating image for quote");
            quoteImageGenerator.generateAndUploadQuoteImage();
            
            // Step 3: Post to Instagram
            logger.info("Posting to Instagram");
            String result = instagramPostService.postLatestQuoteToInstagram();
            logger.info("Instagram posting result: {}", result);
            
            logger.info("Completed scheduled quote generation and posting process");
        } catch (Exception e) {
            logger.error("Error in scheduled quote generation and posting process", e);
        }
    }
}
