package com.wn.wandernest.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wn.wandernest.configs.PlacesApiConfig;
import com.wn.wandernest.dtos.ActivityDTO;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.dtos.PlacesResponse;
import com.wn.wandernest.enums.ActivityInterest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityApiClient {
        private final RestTemplate restTemplate;
        private final PlacesApiConfig config;

        public List<ActivityDTO> fetchActivities(Location location, List<ActivityInterest> interests) {
                // Build URL with activity filters
                // String interestQuery = interests.stream()
                // .map(interest -> interest.name().toLowerCase())
                // .collect(Collectors.joining(","));
                String url = String.format("%s?location=%s&key=%s",
                                config.getBaseUrl(),
                                URLEncoder.encode(location.toString(), StandardCharsets.UTF_8),
                                config.getApiKey());

                // Call API
                ResponseEntity<PlacesResponse> response = restTemplate.postForEntity(url, null, PlacesResponse.class);
                PlacesResponse responseBody = response.getBody();
                if (responseBody == null || responseBody.getPlaces() == null) {
                        return List.of();
                }
                // Map response to Activity entities
                return responseBody.getPlaces().stream()
                                .map(apiItem -> ActivityDTO.builder()
                                                .name(apiItem.displayName.text)
                                                .priceLevel(apiItem.priceLevel)
                                                .id(apiItem.id).address(apiItem.formattedAddress)
                                                .location(new Location(apiItem.location.latitude,
                                                                apiItem.location.longitude))
                                                .rating(apiItem.rating)
                                                .photoName(apiItem.photos.get(0).name)
                                                .priceRange(apiItem.priceRange)
                                                .websiteUri(apiItem.websiteUri)
                                                .build())
                                .toList();
        }
}
