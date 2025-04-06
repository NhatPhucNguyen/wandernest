package com.wn.wandernest.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wn.wandernest.dtos.AccommodationDTO;
import com.wn.wandernest.dtos.Location;
import com.wn.wandernest.dtos.PlacesResponse;
import com.wn.wandernest.enums.AccommodationType;
import com.wn.wandernest.models.Accommodation;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.repositories.AccommodationRepository;
import com.wn.wandernest.repositories.ItineraryRepository;
import com.wn.wandernest.utils.PlacesUtil;
import com.wn.wandernest.utils.UserUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccommodationApiClient {
    private final ItineraryRepository itineraryRepository;
    private final AccommodationRepository accommodationRepository;
    private final PhotoGen photoGen;
    private final PlacesUtil placesUtil;
    // Cache accommodations to reduce API calls (e.g., Caffeine)
    public List<AccommodationDTO> fetchAccommodations(Long itineraryId) {

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
                List.of("lodging"),
                10,
                10000.0);
        
        if (responseBody == null || responseBody.getPlaces() == null) {
            return List.of();
        }
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
                            .photoUri(photoGen.generatePhotoUri(apiItem.photos.get(0).name))
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
        accommodation.setPhotoUri(accommodationDTO.getPhotoUri());
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
