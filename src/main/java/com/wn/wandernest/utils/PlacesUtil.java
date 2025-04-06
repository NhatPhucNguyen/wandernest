package com.wn.wandernest.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.wn.wandernest.configs.PlacesApiConfig;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.dtos.PlacesResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j // Added for logging
public class PlacesUtil {

    private final RestTemplate restTemplate; // Injected by RequiredArgsConstructor
    private final PlacesApiConfig config; // Injected by RequiredArgsConstructor

    /**
     * A utility method to make a request to the Google Places API.
     *
     * @param location      The location to search near
     * @param includedTypes List of place types to include in the search (e.g.,
     *                      ["lodging"], ["tourist_attraction"])
     * @param maxResults    Maximum number of results to return
     * @param radius        Search radius in meters
     * @return Response from the Places API, or null if the request failed
     */
    public PlacesResponse makeGooglePlacesRequest(Location location,
            List<String> includedTypes,
            int maxResults,
            double radius) {
        // Build the types array as JSON
        String typesJson = includedTypes.stream()
                .map(type -> "\"" + type + "\"")
                .collect(Collectors.joining(",", "[", "]"));

        // Build request body
        String requestBodyJson = String.format(
                "{\r\n  \"includedTypes\": %s,\r\n  \"maxResultCount\": %d,\r\n" +
                        "  \"locationRestriction\": {\r\n    \"circle\": {\r\n      \"center\": {\r\n" +
                        "        \"latitude\": %f,\r\n        \"longitude\": %f},\r\n" +
                        "      \"radius\": %.1f\r\n    }\r\n  }\r\n}",
                typesJson, maxResults, location.getLat(), location.getLng(), radius);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", config.getApiKey());
        headers.set("X-Goog-FieldMask", config.getFieldsInclude());

        // Create HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(requestBodyJson, headers);

        try {
            // Call Google Maps API
            ResponseEntity<PlacesResponse> response = restTemplate.postForEntity(
                    config.getBaseUrl(), entity, PlacesResponse.class);

            return response.getBody();
        } catch (RestClientException e) {
            // Log the error and return null
            log.error("Error calling Google Places API: {}", e.getMessage(), e);
            return null;
        }
    }
}