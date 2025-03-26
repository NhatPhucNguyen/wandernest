package com.wn.wandernest.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.wn.wandernest.enums.ItineraryStatus;
import com.wn.wandernest.models.Itinerary;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItineraryResponseDTO {
        private Long id;
        private String destination;
        private LocalDate startDate;
        private LocalDate endDate;
        private double totalBudget;
        private Location location;
        private ItineraryStatus status;
        private List<AccommodationDTO> accommodations;
        private List<ActivityDTO> activities;
        private List<RestaurantDTO> restaurants;
        private int travelers;
        private TravelPreferencesDTO travelPreferences;

    public ItineraryResponseDTO(Itinerary itinerary) {
        this.id = itinerary.getId();
        this.destination = itinerary.getDestination();
        this.startDate = itinerary.getStartDate();
        this.endDate = itinerary.getEndDate();
        this.totalBudget = itinerary.getBudgetAllocation().getTotalBudget();
        this.status = itinerary.getStatus();
        this.travelers = itinerary.getNumberOfTravelers();
        this.travelPreferences = new TravelPreferencesDTO(itinerary.getTravelPreferences());
        this.location = new Location(itinerary.getLat(), itinerary.getLng());
        this.accommodations = Optional.ofNullable(itinerary.getAccommodations())
                .orElse(List.of())
                .stream()
                .map(item -> AccommodationDTO.builder().name(item.getName()).build())
                .collect(Collectors.toList());
        this.activities = Optional.ofNullable(itinerary.getActivities())
                .orElse(List.of())
                .stream()
                .map(item -> ActivityDTO.builder().name(item.getName()).build())
                .collect(Collectors.toList());
        this.restaurants = Optional.ofNullable(itinerary.getRestaurants())
                .orElse(List.of())
                .stream()
                .map(item -> RestaurantDTO.builder().name(item.getName()).build())
                .collect(Collectors.toList());
    }
}