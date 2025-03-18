package com.wn.wandernest.models;

import java.util.List;

import com.wn.wandernest.enums.AccommodationType;
import com.wn.wandernest.models.base.PlaceBaseModel;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accommodations")
public class Accommodation extends PlaceBaseModel {
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<AccommodationType> types; // HOTEL, HOSTEL, etc.
    
    @ManyToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;
}
