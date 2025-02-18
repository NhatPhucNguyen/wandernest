package com.wn.wandernest.dtos;

import java.time.LocalDate;
import java.util.List;

import com.wn.wandernest.models.Itinerary;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItineraryResponseDTO {
    private Long id;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalBudget;
    private BudgetAllocationDTO budgetAllocation;

    public ItineraryResponseDTO(Itinerary itinerary) {
        this.id = itinerary.getId();
        this.destination = itinerary.getDestination();
        this.startDate = itinerary.getStartDate();
        this.endDate = itinerary.getEndDate();
        this.totalBudget = itinerary.getTotalBudget();
        this.budgetAllocation = new BudgetAllocationDTO(itinerary.getBudgetAllocation());
    }
}
