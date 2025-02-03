package com.wn.wandernest.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.wn.wandernest.configs.AccommodationApiConfig;
import com.wn.wandernest.dtos.GoogleMapsResponse;
import com.wn.wandernest.enums.AccommodationType;
import com.wn.wandernest.models.Accommodation;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccommodationApiClient {
    private final RestTemplate restTemplate;
    private final AccommodationApiConfig config; // Contains API key, base URL, etc.

    // Cache accommodations to reduce API calls (e.g., Caffeine)
    public List<Accommodation> fetchAccommodations(String destination, AccommodationType type, double maxPrice) {
        // Build API URL with parameters
        String url = String.format("%s?location=%s&type=%s&maxprice=%s&key=%s",
            config.getBaseUrl(),
            geocodeDestination(destination), // Convert destination to lat/lng
            type.name().toLowerCase(),
            (int) maxPrice,
            config.getApiKey()
        );

        // Call Google Maps API
        ResponseEntity<GoogleMapsResponse> response = restTemplate.getForEntity(url, GoogleMapsResponse.class);

        // Map API response to Accommodation entities
        return response.getBody().getResults().stream()
            .map(apiItem -> Accommodation.builder()
                .name(apiItem.getName())
                .address(apiItem.getVicinity())
                .pricePerNight(apiItem.getPriceLevel() * 50) // Example pricing logic
                .type(type)
                .build())
            .toList();
    }

    private String geocodeDestination(String destination) {
        // Call Google Geocoding API to convert destination to coordinates
        return "48.8566,2.3522"; //example
    }
}
