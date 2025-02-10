package org.codingdsa.quotegenesis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codingdsa.quotegenesis.model.QuotePhoto;
import org.codingdsa.quotegenesis.repository.QuotePhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImageUploadService {

    @Value("${imgHippo.api.key}")
    private String imgHippoApiKey;

    @Value("${freeimage.api.key}")
    private String freeimageApiKey;

    @Autowired
    private QuotePhotoRepository quotePhotoRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String IMG_HIPPO_UPLOAD_URL = "https://api.imghippo.com/v1/upload";

    private static final String FREE_IMAGE_UPLOAD_URL = "https://freeimage.host/api/1/upload";

    /**
     * Uploads an image to Imgbb via multipart file upload.
     * @param imagePath The path of the image file.
     */
    public void uploadImageViaImgHippo(String imagePath) throws Exception {
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new Exception("Image file not found: " + imagePath);
        }

        // Extract quote ID from filename
        String quoteId = extractQuoteId(imageFile.getName());
        if (quoteId == null) {
            throw new Exception("Invalid filename format: " + imageFile.getName());
        }

        // Prepare multipart request body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("api_key", imgHippoApiKey);
        body.add("file", new FileSystemResource(imageFile));

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create request entity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Execute POST request
        ResponseEntity<String> response = restTemplate.exchange(IMG_HIPPO_UPLOAD_URL, HttpMethod.POST, requestEntity, String.class);

        // Print and return response
        System.out.println("Upload Response: " + response.getBody());
        // Parse response and extract uploaded image URL
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.getBody());

        if (jsonResponse.has("data")) {
            String imageUrl = jsonResponse.path("data").path("url").asText();
            saveImageUpload(quoteId, imageUrl);
        } else {
            throw new Exception("Failed to upload image: " + jsonResponse.toString());
        }
    }

    public void uploadImageViaFreeImageHost(String imagePath) throws Exception {
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new Exception("Image file not found: " + imagePath);
        }

        // Extract quote ID from filename
        String quoteId = extractQuoteId(imageFile.getName());
        if (quoteId == null) {
            throw new Exception("Invalid filename format: " + imageFile.getName());
        }

        // Prepare multipart request body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("key", freeimageApiKey);
        body.add("action", "upload");
        body.add("source", new FileSystemResource(imageFile));
        body.add("format", "json");

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create request entity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Execute POST request
        ResponseEntity<String> response = restTemplate.exchange(FREE_IMAGE_UPLOAD_URL, HttpMethod.POST, requestEntity, String.class);

        // Print and return response
        System.out.println("Upload Response: " + response.getBody());
        // Parse response and extract uploaded image URL
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.getBody());

        if (jsonResponse.has("image")) {
            String imageUrl = jsonResponse.path("image").path("url").asText();
            saveImageUpload(quoteId, imageUrl);
        } else {
            throw new Exception("Failed to upload image: " + jsonResponse.toString());
        }
    }

    /**
     * Extracts quote ID from the filename (expected format: quote_<id>.png).
     * @param filename The image filename.
     * @return Extracted quote ID or null if not found.
     */
    private String extractQuoteId(String filename) {
        Pattern pattern = Pattern.compile("quote_(\\d+)\\.jpg");
        Matcher matcher = pattern.matcher(filename);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * Saves image URL to the database.
     * @param quoteId The extracted quote ID.
     * @param imageUrl The URL of the uploaded image.
     */
    private void saveImageUpload(String quoteId, String imageUrl) {
        QuotePhoto quotePhoto = new QuotePhoto(null, quoteId, imageUrl);
        quotePhotoRepository.save(quotePhoto);
    }
}
