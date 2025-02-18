package com.wn.wandernest.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wn.wandernest.configs.RestaurantApiConfig;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.dtos.PlacesResponse;
import com.wn.wandernest.dtos.RestaurantDTO;
import com.wn.wandernest.enums.Cuisine;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantApiClient {
        private final RestTemplate restTemplate;
        private final RestaurantApiConfig config;

        public List<RestaurantDTO> fetchRestaurants(Location location, List<Cuisine> cuisines) {
                // TODO: Build URL with cuisine filters
                String url = String.format("%s?location=%s&key=%s",
                                config.getBaseUrl(),
                                URLEncoder.encode(location.toString(), StandardCharsets.UTF_8),
                                config.getApiKey());

                // Call TripAdvisor API
                ResponseEntity<PlacesResponse> response = restTemplate.postForEntity(url, null, PlacesResponse.class);
                PlacesResponse responseBody = response.getBody();
                if (responseBody == null || responseBody.getPlaces() == null) {
                        return List.of();
                }
                // Map response to Restaurant entities
                return responseBody.getPlaces().stream()
                                .map(apiItem -> RestaurantDTO.builder()
                                                .name(apiItem.displayName.text)
                                                .priceLevel(apiItem.priceLevel)
                                                .id(apiItem.id).address(apiItem.formattedAddress)
                                                .location(new Location(apiItem.location.latitude,
                                                                apiItem.location.longitude))
                                                .photoName(apiItem.photos.get(0).name)
                                                .priceRange(apiItem.priceRange)
                                                .websiteUri(apiItem.websiteUri)
                                                .rating(apiItem.rating)
                                                .build())
                                .toList();
        }
}
