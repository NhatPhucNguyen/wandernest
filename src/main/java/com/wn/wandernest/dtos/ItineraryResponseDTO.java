package com.wn.wandernest.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.wn.wandernest.models.Accommodation;
import com.wn.wandernest.models.Activity;
import com.wn.wandernest.models.BudgetAllocation;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.models.Restaurant;

import lombok.AllArgsConstructor;
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
    private List<AccommodationDTO> accommodations;
    private List<ActivityDTO> activities;
    private List<RestaurantDTO> restaurants;
    private BudgetAllocationDTO budgetAllocation;

    public ItineraryResponseDTO(Itinerary itinerary) {
        this.id = itinerary.getId();
        this.destination = itinerary.getDestination();
        this.startDate = itinerary.getStartDate();
        this.endDate = itinerary.getEndDate();
        this.totalBudget = itinerary.getTotalBudget();
        this.accommodations = itinerary.getAccommodations().stream()
                .map(AccommodationDTO::new)
                .collect(Collectors.toList());
        this.activities = itinerary.getActivities().stream()
                .map(ActivityDTO::new)
                .collect(Collectors.toList());
        this.restaurants = itinerary.getRestaurants().stream()
                .map(RestaurantDTO::new)
                .collect(Collectors.toList());
        this.budgetAllocation = new BudgetAllocationDTO(itinerary.getBudgetAllocation());
    }
}

// Example nested DTOs:
@Data
@NoArgsConstructor
class AccommodationDTO {
    private String name;
    private double pricePerNight;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    public AccommodationDTO(Accommodation accommodation) {
        this.name = accommodation.getName();
        this.pricePerNight = accommodation.getPricePerNight();
    }
}

@Data
@NoArgsConstructor
class ActivityDTO {
    private String name;
    private double cost;
    private LocalDate date;
    public ActivityDTO (Activity activity){
        this.name = activity.getName();
        this.cost = activity.getCost();
        this.date = activity.getDate();
    }
}

@Data
@NoArgsConstructor
class RestaurantDTO {
    private String name;
    private String cuisine;
    private double priceRange;
    private LocalDate date;
    public RestaurantDTO (Restaurant restaurant){
        this.name = restaurant.getName();
        this.cuisine = restaurant.getCuisine();
        this.priceRange = restaurant.getPriceRange();
        this.date = restaurant.getDate();
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class BudgetAllocationDTO {
    private double accommodation;
    private double meals;
    private double activities;
    private double transportation;
    private double miscellaneous;
    public BudgetAllocationDTO(BudgetAllocation budgetAllocation) {
        this.accommodation = budgetAllocation.getAccommodation();
        this.meals = budgetAllocation.getMeals();
        this.activities = budgetAllocation.getActivities();
        this.transportation = budgetAllocation.getTransportation();
        this.miscellaneous = budgetAllocation.getMiscellaneous();
    }
}
