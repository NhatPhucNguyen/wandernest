package com.wn.wandernest.models;

import com.wn.wandernest.models.base.PlaceBaseModel;

import jakarta.persistence.Entity;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "activities")
public class Activity extends PlaceBaseModel {

    @ManyToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;
}
