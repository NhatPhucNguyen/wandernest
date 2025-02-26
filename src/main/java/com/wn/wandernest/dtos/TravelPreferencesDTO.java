package com.wn.wandernest.dtos;

import java.util.List;

import com.wn.wandernest.enums.AccommodationType;
import com.wn.wandernest.enums.ActivityInterest;
import com.wn.wandernest.enums.Cuisine;
import com.wn.wandernest.models.TravelPreferences;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelPreferencesDTO {
    private AccommodationType accommodationType;
    private List<Cuisine> cuisinePreferences;
    private List<ActivityInterest> activityInterests;
    public TravelPreferencesDTO(TravelPreferences travelPreferences){
        this.accommodationType = travelPreferences.getAccommodationType();
        this.cuisinePreferences = travelPreferences.getCuisinePreferences();
        this.activityInterests = travelPreferences.getActivityInterests();
    }
}
