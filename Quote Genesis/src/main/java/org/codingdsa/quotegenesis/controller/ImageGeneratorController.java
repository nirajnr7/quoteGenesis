package org.codingdsa.quotegenesis.controller;

import org.codingdsa.quotegenesis.service.ImageUploadPOC;
import org.codingdsa.quotegenesis.service.QuoteImageGenerator;
import org.codingdsa.quotegenesis.service.InstagramPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ImageGeneratorController {
    @Autowired
    private QuoteImageGenerator quoteImageGenerator;

    @Autowired
    private ImageUploadPOC imageUploadPOC;

    @Autowired
    private InstagramPostService instagramPostService;

    @GetMapping("/generateImage")
    public String generateImage() throws Exception {
        quoteImageGenerator.generateAndUploadQuoteImage();
        return "ResponseEntity.ok()";
    }

    @GetMapping("/imageUploadPOC")
    public String imageUploadPOC() throws Exception {
        imageUploadPOC.uploadImage();
        return "ResponseEntity.ok()";
    }

    @GetMapping("/postToInstagram")
    public String postToInstagram() throws Exception {
        return instagramPostService.postLatestQuoteToInstagram();
    }
}
