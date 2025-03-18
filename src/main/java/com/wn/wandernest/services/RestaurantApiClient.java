package com.wn.wandernest.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wn.wandernest.configs.PlacesApiConfig;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.dtos.PlacesResponse;
import com.wn.wandernest.dtos.RestaurantDTO;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.models.Restaurant;
import com.wn.wandernest.repositories.ItineraryRepository;
import com.wn.wandernest.repositories.RestaurantRepository;
import com.wn.wandernest.utils.UserUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantApiClient {
        private final RestTemplate restTemplate;
        private final PlacesApiConfig config;
        private final ItineraryRepository itineraryRepository;
        private final RestaurantRepository restaurantRepository;
        private final PhotoGen photoGen;
        public List<RestaurantDTO> fetchRestaurants(Long itineraryId) {
                Itinerary itinerary = itineraryRepository.findById(itineraryId)
                                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));
                // Check if userId is not equal to itinerary user id
                Long currentUserId = UserUtils.getCurrentUserId();
                if (!itinerary.getUser().getId().equals(currentUserId)) {
                        throw new IllegalArgumentException("User ID does not match the itinerary owner");
                }
                // get location from itinerary
                Location location = new Location(itinerary.getLat(), itinerary.getLng());
                String url = String.format("%s/restaurants?location=%s&key=%s",
                                config.getBaseUrl(),
                                URLEncoder.encode(location.toString(), StandardCharsets.UTF_8),
                                config.getApiKey());

                // Call TripAdvisor API
                ResponseEntity<PlacesResponse> response = restTemplate.postForEntity(url, null, PlacesResponse.class);
                PlacesResponse responseBody = response.getBody();
                if (responseBody == null || responseBody.getPlaces() == null) {
                        return List.of();
                }
                // fetch existing restaurant
                List<Restaurant> existingRestaurants = restaurantRepository.findByItineraryId(itineraryId);
                // Map response to Restaurant entities
                return responseBody.getPlaces().stream()
                                .map(apiItem -> {
                                        boolean isSaved = existingRestaurants.stream()
                                                        .anyMatch(restaurant -> restaurant.getId()
                                                                        .equals(apiItem.id));
                                        return RestaurantDTO.builder()
                                                        .name(apiItem.displayName.text)
                                                        .priceLevel(apiItem.priceLevel)
                                                        .id(apiItem.id).address(apiItem.formattedAddress)
                                                        .location(new Location(apiItem.location.latitude,
                                                                        apiItem.location.longitude))
                                                        .photoUri(photoGen.generatePhotoUri(apiItem.photos.get(0).name))
                                                        .priceRange(apiItem.priceRange)
                                                        .websiteUri(apiItem.websiteUri)
                                                        .rating(apiItem.rating)
                                                        .isSaved(isSaved)
                                                        .weekdayDescriptions(
                                                                        apiItem.getCurrentOpeningHours().weekdayDescriptions)
                                                        .build();
                                })
                                .toList();
        }

        public void saveRestaurantByItinerary(Long itineraryId, RestaurantDTO restaurantDTO) {
                Itinerary itinerary = itineraryRepository.findById(itineraryId)
                                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));

                // Check if userId is not equal to itinerary user id
                Long currentUserId = UserUtils.getCurrentUserId();
                if (!itinerary.getUser().getId().equals(currentUserId)) {
                        throw new IllegalArgumentException("User ID does not match the itinerary owner");
                }

                // Convert RestaurantDTO to Restaurant entity
                Restaurant restaurant = new Restaurant();
                restaurant.setName(restaurantDTO.getName());
                restaurant.setAddress(restaurantDTO.getAddress());
                restaurant.setPriceLevel(restaurantDTO.getPriceLevel());
                restaurant.setLat(restaurantDTO.getLocation().getLat());
                restaurant.setLng(restaurantDTO.getLocation().getLng());
                restaurant.setPhotoUri(restaurantDTO.getPhotoUri());
                restaurant.setWebsiteUri(restaurantDTO.getWebsiteUri());
                restaurant.setRating(restaurantDTO.getRating());
                restaurant.setItinerary(itinerary);
                restaurant.setId(restaurantDTO.getId());
                // Save restaurant to the repository
                restaurantRepository.save(restaurant);
        }

        public void unsaveRestaurantByItinerary(Long itineraryId, String restaurantId) {
                Itinerary itinerary = itineraryRepository.findById(itineraryId)
                                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));

                // Check if userId is not equal to itinerary user id
                Long currentUserId = UserUtils.getCurrentUserId();
                if (!itinerary.getUser().getId().equals(currentUserId)) {
                        throw new IllegalArgumentException("User ID does not match the itinerary owner");
                }

                // Find the restaurant by id and itinerary
                Restaurant restaurant = restaurantRepository.findByIdAndItineraryId(restaurantId, itineraryId)
                                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

                // Delete the restaurant from the repository
                restaurantRepository.delete(restaurant);
        }
}
