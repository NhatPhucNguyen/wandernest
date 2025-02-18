package com.wn.wandernest.models;

import com.wn.wandernest.enums.ActivityType;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "activities")
public class Activity {
    @Id
    private Long id;
    
    private String name;
    private String address;
    private String priceLevel;
    private String photoName;
    private double lat;
    private double lng;
    private double rating;
    private String websiteUri;
    
    @Enumerated(EnumType.STRING)
    private ActivityType type; // SIGHTSEEING, ADVENTURE, etc.
    
    @ManyToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;
}
