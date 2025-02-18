package com.wn.wandernest.dtos;

import com.wn.wandernest.dtos.PlacesResponse.PlaceData.PriceRange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDTO {
    private String id;
    private String name;
    private String address;
    private String priceLevel;
    private String photoName;
    private PriceRange priceRange;
    private Location location;
    private String websiteUri;
    private double rating;
}
