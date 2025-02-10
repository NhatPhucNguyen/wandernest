package com.wn.wandernest.dtos;

import java.time.LocalDate;
import java.util.List;

import com.wn.wandernest.enums.AccommodationType;
import com.wn.wandernest.enums.ActivityInterest;
import com.wn.wandernest.enums.Cuisine;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItineraryRequestDTO {
    private String destination;
    private Location location;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfTravelers;
    private double totalBudget;
    private AccommodationType accommodationType;
    private List<Cuisine> cuisinePreferences;
    private List<ActivityInterest> activityInterests;
}
