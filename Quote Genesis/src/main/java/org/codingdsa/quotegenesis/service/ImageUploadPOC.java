package org.codingdsa.quotegenesis.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Paths;

@Service
public class ImageUploadPOC {

    private static final String API_KEY = "6d207e02198a847aa98d0a2a901485a5";
    private static final String UPLOAD_URL = "https://freeimage.host/api/1/upload";
    private final RestTemplate restTemplate = new RestTemplate();

    public String uploadImage() {
        try {
            // Get absolute path of the image file
            String imagePath = Paths.get("src/main/resources/output/quote_12.jpg").toAbsolutePath().toString();
            File imageFile = new File(imagePath);

            if (!imageFile.exists()) {
                System.err.println("Error: Image file not found at " + imageFile.getAbsolutePath());
                return "{\"error\": \"Image file not found\"}";
            }

            // Prepare multipart request body
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("key", API_KEY);
            body.add("action", "upload");
            body.add("source", new FileSystemResource(imageFile));
            body.add("format", "json");

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Create request entity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Execute POST request
            ResponseEntity<String> response = restTemplate.exchange(UPLOAD_URL, HttpMethod.POST, requestEntity, String.class);

            // Print and return response
            System.out.println("Upload Response: " + response.getBody());
            return response.getBody();

        } catch (Exception e) {
            System.err.println("Failed to upload image: " + e.getMessage());
            return "{\"error\": \"Failed to upload image\"}";
        }
    }
}