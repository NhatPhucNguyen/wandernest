package com.wn.wandernest.models;

import java.util.List;

import com.wn.wandernest.enums.AccommodationType;
import com.wn.wandernest.enums.ActivityInterest;
import com.wn.wandernest.enums.Cuisine;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "travel_preferences")
public class TravelPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private AccommodationType accommodationType; // HOTEL, HOSTEL, AIRBNB
    
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Cuisine> cuisinePreferences; // ITALIAN, VEGETARIAN, etc.
    
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ActivityInterest> activityInterests; // ADVENTURE, CULTURAL, etc.
    
    @OneToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;
}
