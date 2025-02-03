package com.wn.wandernest.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wn.wandernest.dtos.ItineraryRequestDTO;
import com.wn.wandernest.enums.ItineraryStatus;
import com.wn.wandernest.models.Accommodation;
import com.wn.wandernest.models.Activity;
import com.wn.wandernest.models.BudgetAllocation;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.models.Restaurant;
import com.wn.wandernest.repositories.ItineraryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItineraryService {
    private final AccommodationApiClient accommodationApiClient;
    private final RestaurantApiClient restaurantApiClient;
    private final ActivityApiClient activityApiClient;
    private final ItineraryRepository itineraryRepository;

    public Itinerary generateItinerary(ItineraryRequestDTO request) {
        // 1. Validate input
        validateRequest(request);

        // 2. Fetch accommodations from API
        List<Accommodation> accommodations = accommodationApiClient.fetchAccommodations(
            request.getDestination(),
            request.getAccommodationType(),
            request.getTotalBudget()
        );

        // 3. Fetch restaurants from API
        List<Restaurant> restaurants = restaurantApiClient.fetchRestaurants(
            request.getDestination(),
            request.getCuisinePreferences()
        );

        // 4. Fetch activities from API
        List<Activity> activities = activityApiClient.fetchActivities(
            request.getDestination(),
            request.getActivityInterests()
        );

        // 5. Allocate budget
        BudgetAllocation budget = allocateBudget(request.getTotalBudget());

        // 6. Build and save itinerary
        Itinerary itinerary = Itinerary.builder()
            .destination(request.getDestination())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .numberOfTravelers(request.getNumberOfTravelers())
            .totalBudget(request.getTotalBudget())
            .status(ItineraryStatus.DRAFT)
            .accommodations(accommodations)
            .restaurants(restaurants)
            .activities(activities)
            .budgetAllocation(budget)
            .build();

        return itineraryRepository.save(itinerary);
    }

    private BudgetAllocation allocateBudget(double totalBudget) {
        // Example: Simple budget allocation logic
        BudgetAllocation budget = new BudgetAllocation();
        budget.setAccommodation(totalBudget * 0.4);
        budget.setMeals(totalBudget * 0.3);
        budget.setActivities(totalBudget * 0.2);
        budget.setTransportation(totalBudget * 0.1);
        return budget;
    }

    private void validateRequest(ItineraryRequestDTO request) {
        // Validate dates, budget, etc.
    }
}
