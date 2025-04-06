package com.wn.wandernest.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class PlacesApiConfig {
    @Value("https://places.googleapis.com/v1/places:searchNearby")
    private String baseUrl;
    @Value("${google.key}")
    private String apiKey;
    @Value("${google.accessToken}")
    private String accessKey;
    private int maxRetries = 1;
    @Value("places.priceRange,places.displayName,places.location,places.formattedAddress,places.types,places.websiteUri,places.photos,places.rating,places.id,places.currentOpeningHours,places.priceLevel,places.googleMapsLinks")
    private String fieldsInclude;
}
