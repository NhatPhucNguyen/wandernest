package com.wn.wandernest.models;

import java.util.List;

import com.wn.wandernest.enums.AccommodationType;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accommodations")
public class Accommodation {
    @Id
    private String id;
    
    private String name;
    private String address;
    private String priceLevel;
    private String photoName;
    private double startPrice;
    private double endPrice;
    private double lat;
    private double lng;
    private double rating;
    private String websiteUri;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<AccommodationType> types; // HOTEL, HOSTEL, etc.
    
    @ManyToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;
}
