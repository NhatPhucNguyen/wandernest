package com.wn.wandernest.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wn.wandernest.configs.PlacesApiConfig;
import com.wn.wandernest.dtos.AccommodationDTO;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.dtos.PlacesResponse;
import com.wn.wandernest.enums.AccommodationType;
import com.wn.wandernest.models.Accommodation;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.repositories.AccommodationRepository;
import com.wn.wandernest.repositories.ItineraryRepository;
import com.wn.wandernest.utils.UserUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccommodationApiClient {
    private final RestTemplate restTemplate;
    private final PlacesApiConfig config; // Contains API key, base URL, etc.
    private final ItineraryRepository itineraryRepository;
    private final AccommodationRepository accommodationRepository;

    // Cache accommodations to reduce API calls (e.g., Caffeine)
    public List<AccommodationDTO> fetchAccommodations(Long itineraryId) {

        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));
        // Check if userId is not equal to itinerary user id
        Long currentUserId = UserUtils.getCurrentUserId();
        if (!itinerary.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("User ID does not match the itinerary owner");
        }
        // get location from itinerary
        Location location = new Location(itinerary.getLat(), itinerary.getLng());
        // // Build request body
        // String requestBodyJson = String.format(
        // "{\r\n \"includedTypes\": [\"lodging\"],\r\n \"maxResultCount\": 10,\r\n
        // \"locationRestriction\": {\r\n \"circle\": {\r\n \"center\": {\r\n
        // \"latitude\": %f,\r\n \"longitude\": %f},\r\n \"radius\": 10000.0\r\n }\r\n
        // }\r\n}",
        // location.getLat(), location.getLng());

        // // Set headers
        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);
        // headers.set("X-Goog-Api-Key", config.getApiKey());
        // headers.set("X-Goog-FieldMask",
        // "places.priceRange,places.displayName,places.location,places.formattedAddress,places.types,places.websiteUri,places.photos,places.rating,places.id,places.currentOpeningHours,places.priceLevel,places.googleMapsLinks");

        // // Create HttpEntity
        // HttpEntity<String> entity = new HttpEntity<>(requestBodyJson, headers);

        // // Build API URL
        // String url = String.format("%s/places:searchNearby", config.getBaseUrl());

        // // Call Google Maps API
        // ResponseEntity<PlacesResponse> response = restTemplate.postForEntity(url,
        // entity, PlacesResponse.class);
        // PlacesResponse responseBody = response.getBody();
        // if (responseBody == null || responseBody.getPlaces() == null) {
        // return List.of();
        // }
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
        // Fetch existing accommodations from the database
        List<Accommodation> existingAccommodations = accommodationRepository.findByItineraryId(itineraryId);
        return responseBody.getPlaces().stream()
                .map(apiItem -> {
                    boolean isSaved = existingAccommodations.stream()
                            .anyMatch(accommodation -> accommodation.getId().equals(apiItem.id));
                    return AccommodationDTO.builder()
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
                            .isSaved(isSaved)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void saveAccommodationByItinerary(Long itineraryId, AccommodationDTO accommodationDTO) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));

        // Check if userId is not equal to itinerary user id
        Long currentUserId = UserUtils.getCurrentUserId();
        if (!itinerary.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("User ID does not match the itinerary owner");
        }

        // Convert AccommodationDTO to Accommodation entity
        Accommodation accommodation = new Accommodation();
        accommodation.setName(accommodationDTO.getName());
        accommodation.setAddress(accommodationDTO.getAddress());
        accommodation.setPriceLevel(accommodationDTO.getPriceLevel());
        accommodation.setTypes(accommodationDTO.getTypes());
        accommodation.setLat(accommodationDTO.getLocation().getLat());
        accommodation.setLng(accommodationDTO.getLocation().getLng());
        accommodation.setPhotoName(accommodationDTO.getPhotoName());
        accommodation.setWebsiteUri(accommodationDTO.getWebsiteUri());
        accommodation.setRating(accommodationDTO.getRating());
        accommodation.setItinerary(itinerary);
        accommodation.setId(accommodationDTO.getId());
        // Save accommodation to the repository
        accommodationRepository.save(accommodation);
    }
    public void unsaveAccommodationByItinerary(Long itineraryId, String accommodationId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));

        // Check if userId is not equal to itinerary user id
        Long currentUserId = UserUtils.getCurrentUserId();
        if (!itinerary.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("User ID does not match the itinerary owner");
        }

        // Find the accommodation by id and itinerary
        Accommodation accommodation = accommodationRepository.findByIdAndItineraryId(accommodationId, itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation not found"));

        // Delete the accommodation from the repository
        accommodationRepository.delete(accommodation);
    }
}
