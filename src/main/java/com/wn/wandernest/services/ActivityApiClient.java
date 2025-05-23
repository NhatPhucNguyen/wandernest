package com.wn.wandernest.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wn.wandernest.dtos.ActivityDTO;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.dtos.PlacesResponse;
import com.wn.wandernest.models.Activity;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.repositories.ActivityRepository;
import com.wn.wandernest.repositories.ItineraryRepository;
import com.wn.wandernest.utils.PlacesUtil;
import com.wn.wandernest.utils.UserUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityApiClient {
    private final ItineraryRepository itineraryRepository;
    private final ActivityRepository activityRepository;
    private final PhotoGen photoGen;
    private final PlacesUtil placesUtil;
    public List<ActivityDTO> fetchActivities(Long itineraryId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));
        // Check if userId is not equal to itinerary user id
        Long currentUserId = UserUtils.getCurrentUserId();
        if (!itinerary.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("User ID does not match the itinerary owner");
        }
        // Create Location object from itinerary coordinates
        Location location = new Location(itinerary.getLat(), itinerary.getLng());
        // Use PlacesUtil to make the API call
        PlacesResponse responseBody = placesUtil.makeGooglePlacesRequest(
                location,
                List.of("tourist_attraction"),
                10,
                10000.0);
        if (responseBody == null || responseBody.getPlaces() == null) {
            return List.of();
        }
        // fetch existing activities
        List<Activity> existingActivities = activityRepository.findByItineraryId(itineraryId);
        // Map response to Activity entities
        return responseBody.getPlaces().stream()
                .map(apiItem -> {
                    boolean isSaved = existingActivities.stream()
                            .anyMatch(activity -> activity.getId().equals(apiItem.id));
                    return ActivityDTO.builder()
                            .name(apiItem.displayName.text)
                            .priceLevel(apiItem.priceLevel)
                            .id(apiItem.id).address(apiItem.formattedAddress)
                            .location(new Location(apiItem.location.latitude,
                                    apiItem.location.longitude))
                            .rating(apiItem.rating)
                            .photoUri(photoGen.generatePhotoUri(apiItem.photos.get(0).name))
                            .priceRange(apiItem.priceRange)
                            .websiteUri(apiItem.websiteUri)
                            .isSaved(isSaved)
                            .build();
                })
                .toList();
    }

    public void saveActivityByItinerary(Long itineraryId, ActivityDTO activityDTO) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));

        // Check if userId is not equal to itinerary user id
        Long currentUserId = UserUtils.getCurrentUserId();
        if (!itinerary.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("User ID does not match the itinerary owner");
        }

        // Convert ActivityDTO to Activity entity
        Activity activity = new Activity();
        activity.setName(activityDTO.getName());
        activity.setAddress(activityDTO.getAddress());
        activity.setPriceLevel(activityDTO.getPriceLevel());
        activity.setLat(activityDTO.getLocation().getLat());
        activity.setLng(activityDTO.getLocation().getLng());
        activity.setPhotoUri(activityDTO.getPhotoUri());
        activity.setWebsiteUri(activityDTO.getWebsiteUri());
        activity.setRating(activityDTO.getRating());
        activity.setItinerary(itinerary);
        activity.setId(activityDTO.getId());
        // Save activity to the repository
        activityRepository.save(activity);
    }

    public void unsaveActivityByItinerary(Long itineraryId, String activityId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));

        // Check if userId is not equal to itinerary user id
        Long currentUserId = UserUtils.getCurrentUserId();
        if (!itinerary.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("User ID does not match the itinerary owner");
        }

        // Find the activity by id and itinerary
        Activity activity = activityRepository.findByIdAndItineraryId(activityId, itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found"));

        // Delete the activity from the repository
        activityRepository.delete(activity);
    }
}