package com.wn.wandernest.dtos;

import java.util.List;

import com.wn.wandernest.enums.AccommodationType;
import com.wn.wandernest.models.Accommodation;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationDTO {
    private String id;
    private String name;
    private String address;
    private String priceLevel;
    private String photoName;
    private Location location;
    private String websiteUri;
    private double rating;
    private List<AccommodationType> types;
    @Nullable
    private boolean isSaved;

    public AccommodationDTO(Accommodation accommodation) {
        // Initialize fields from the accommodation object
    }
}
