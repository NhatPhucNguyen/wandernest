package com.wn.wandernest.models;

import jakarta.persistence.Entity;
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
@Table(name = "restaurants")
public class Restaurant {
    @Id
    private Long id;

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

    @ManyToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;
}
