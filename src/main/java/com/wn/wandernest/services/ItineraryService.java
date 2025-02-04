package com.wn.wandernest.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
                request.getTotalBudget());

        // 3. Fetch restaurants from API
        List<Restaurant> restaurants = restaurantApiClient.fetchRestaurants(
                request.getDestination(),
                request.getCuisinePreferences());

        // 4. Fetch activities from API
        List<Activity> activities = activityApiClient.fetchActivities(
                request.getDestination(),
                request.getActivityInterests());

        // 5. Allocate budget
        BudgetAllocation budget = allocateBudget(request.getTotalBudget(), request.getNumberOfTravelers(),
                request.getStartDate(), request.getEndDate());

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

    private BudgetAllocation allocateBudget(double totalBudget, int numberOfTravelers, LocalDate startDate,
            LocalDate endDate) {
        // Example: Simple budget allocation logic

        long tripDuration = ChronoUnit.DAYS.between(startDate, endDate);

        // Example: Dynamic budget allocation logic
        double dailyBudgetPerPerson = totalBudget / (tripDuration * numberOfTravelers);

        BudgetAllocation budget = new BudgetAllocation();
        budget.setAccommodation(dailyBudgetPerPerson * 0.4 * tripDuration * numberOfTravelers);
        budget.setMeals(dailyBudgetPerPerson * 0.3 * tripDuration * numberOfTravelers);
        budget.setActivities(dailyBudgetPerPerson * 0.2 * tripDuration * numberOfTravelers);
        budget.setTransportation(dailyBudgetPerPerson * 0.1 * tripDuration * numberOfTravelers);
        return budget;
    }

    private void validateRequest(ItineraryRequestDTO request) {
        // Validate dates, budget, etc.
    }
}
