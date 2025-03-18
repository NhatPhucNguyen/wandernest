package com.wn.wandernest.dtos;

import java.util.List;

import com.wn.wandernest.dtos.PlacesResponse.PlaceData.PriceRange;
import com.wn.wandernest.enums.ActivityInterest;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityDTO {
    private String id;
    private String name;
    private String address;
    private String priceLevel;
    private String photoUri;
    private Location location;
    private String websiteUri;
    private double rating;
    private PriceRange priceRange;
    private List<ActivityInterest> types;
    @Nullable
    private boolean isSaved;
}
