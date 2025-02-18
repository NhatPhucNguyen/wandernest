package com.wn.wandernest.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wn.wandernest.configs.AccommodationApiConfig;
import com.wn.wandernest.dtos.AccommodationDTO;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.dtos.PlacesResponse;
import com.wn.wandernest.enums.AccommodationType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccommodationApiClient {
    private final RestTemplate restTemplate;
    private final AccommodationApiConfig config; // Contains API key, base URL, etc.

    // Cache accommodations to reduce API calls (e.g., Caffeine)
    public List<AccommodationDTO> fetchAccommodations(Location location, AccommodationType type, double maxPrice) {
        // Build API URL with parameters
        String url = String.format("%s?location=%s&type=lodging&key=%s",
                config.getBaseUrl(),
                URLEncoder.encode(location.toString(), StandardCharsets.UTF_8),
                config.getApiKey());
        // Call Google Maps API
        ResponseEntity<PlacesResponse> response = restTemplate.postForEntity(url, null, PlacesResponse.class);
        PlacesResponse responseBody = response.getBody();
        if (responseBody == null || responseBody.getPlaces() == null) {
            return List.of();
        }

        return responseBody.getPlaces().stream()
                .map(apiItem -> AccommodationDTO.builder()
                        .id(apiItem.id)
                        .name(apiItem.displayName.text)
                        .address(apiItem.formattedAddress)
                        .priceLevel(apiItem.priceLevel)
                        .types(apiItem.types.stream()
                                .map(typeStr -> {
                                    try {
                                        return AccommodationType.valueOf(typeStr.toUpperCase());
                                    } catch (IllegalArgumentException e) {
                                        return null; // Handle unknown types
                                    }
                                })
                                .filter(typeEnum -> typeEnum != null)
                                .collect(Collectors.toList()))
                        .location(new Location(apiItem.location.latitude, apiItem.location.longitude))
                        .photoName(apiItem.photos.get(0).name)
                        .websiteUri(apiItem.websiteUri)
                        .rating(apiItem.rating)
                        .build())
                .toList();
    }
}
