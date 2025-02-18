package com.wn.wandernest.dtos;

import java.util.List;

import com.wn.wandernest.dtos.PlacesResponse.PlaceData.PriceRange;

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
    private String photoName;
    private Location location;
    private String websiteUri;
    private double rating;
    private PriceRange priceRange;
    private List<String> types;
}
