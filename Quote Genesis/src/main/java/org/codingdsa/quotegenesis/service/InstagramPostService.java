package org.codingdsa.quotegenesis.service;

import org.codingdsa.quotegenesis.model.QuotePhoto;
import org.codingdsa.quotegenesis.repository.QuotePhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InstagramPostService {

    @Value("${instagram.access.token}")
    private String accessToken;

    @Value("${instagram.business.account.id}")
    private String instagramAccountId;

    @Autowired
    private QuotePhotoRepository quotePhotoRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String GRAPH_API_BASE_URL = "https://graph.facebook.com/v22.0/";

    public String postLatestQuoteToInstagram() throws Exception {
        // 1. Get the latest uploaded image URL
        QuotePhoto latestPhoto = quotePhotoRepository.findAll(
            PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent().get(0);

        if (latestPhoto == null || latestPhoto.getImageUrl() == null) {
            throw new Exception("No image URL found in database");
        }

        // 2. Create container for the post
        String containerId = createContainer(latestPhoto.getImageUrl());
        if (containerId == null) {
            throw new Exception("Failed to create container");
        }

        // 3. Publish the container
        return publishContainer(containerId);
    }

    private String createContainer(String imageUrl) throws Exception {
        String url = GRAPH_API_BASE_URL + instagramAccountId + "/media";

        // Prepare the request body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("image_url", imageUrl);
        body.add("is_carousel_item", "false");
        body.add("caption", "Daily Inspiration ✨ #QuoteOfTheDay #Motivation #Success");
        body.add("access_token", accessToken);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // Execute POST request
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        // Parse response to get container ID
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.getBody());
        
        if (jsonResponse.has("id")) {
            return jsonResponse.get("id").asText();
        }
        
        throw new Exception("Failed to create container: " + response.getBody());
    }

    private String publishContainer(String containerId) throws Exception {
        String url = GRAPH_API_BASE_URL + instagramAccountId + "/media_publish";

        // Prepare the request body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("creation_id", containerId);
        body.add("access_token", accessToken);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // Execute POST request
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        // Parse response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.getBody());
        
        if (jsonResponse.has("id")) {
            return "Successfully published to Instagram with ID: " + jsonResponse.get("id").asText();
        }
        
        throw new Exception("Failed to publish container: " + response.getBody());
    }
}
