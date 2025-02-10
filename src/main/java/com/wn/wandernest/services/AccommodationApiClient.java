package com.wn.wandernest.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.wn.wandernest.configs.AccommodationApiConfig;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.dtos.PlacesResponse;
import com.wn.wandernest.enums.AccommodationType;
import com.wn.wandernest.models.Accommodation;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccommodationApiClient {
    private final RestTemplate restTemplate;
    private final AccommodationApiConfig config; // Contains API key, base URL, etc.

    // Cache accommodations to reduce API calls (e.g., Caffeine)
    public List<Accommodation> fetchAccommodations(Location location, AccommodationType type, double maxPrice) {
        // Build API URL with parameters
        String url = String.format("%s?location=%s&type=lodging&key=%s",
            config.getBaseUrl(),
            URLEncoder.encode(location.toString(),StandardCharsets.UTF_8),
            config.getApiKey()
        );

        // Call Google Maps API
        ResponseEntity<PlacesResponse> response = restTemplate.getForEntity(url, PlacesResponse.class);

        // Map API response to Accommodation entities
        return response.getBody().getResults().stream()
            .map(apiItem -> Accommodation.builder()
                .name(apiItem.getName())
                .address(apiItem.getVicinity())
                .priceLevel(apiItem.getPrice_level()) // Example pricing logic
                .type(type)
                .build())
            .toList();
    }
}
