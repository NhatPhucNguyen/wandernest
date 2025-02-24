package com.wn.wandernest.models;

import java.time.LocalDate;
import java.util.List;

import com.wn.wandernest.enums.ItineraryStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "itineraries")
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfTravelers;
    private double totalBudget;
    
    @Enumerated(EnumType.STRING)
    private ItineraryStatus status; // DRAFT, ACTIVE, COMPLETED
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToOne(mappedBy = "itinerary", cascade = CascadeType.ALL)
    private TravelPreferences travelPreferences;
    
    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL)
    private List<Accommodation> accommodations;
    
    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL)
    private List<Activity> activities;
    
    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL)
    private List<Restaurant> restaurants;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "budget_allocation_id", referencedColumnName = "id")
    private BudgetAllocation budgetAllocation;
}
