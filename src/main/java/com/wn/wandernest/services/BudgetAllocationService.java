package com.wn.wandernest.services;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wn.wandernest.dtos.BudgetAllocationDTO;
import com.wn.wandernest.enums.ExpenseCategory;
import com.wn.wandernest.models.BudgetAllocation;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.repositories.BudgetAllocationRepository;
import com.wn.wandernest.repositories.ItineraryRepository;
import com.wn.wandernest.utils.UserUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetAllocationService {
    private final BudgetAllocationRepository budgetAllocationRepository;
    private final ItineraryRepository itineraryRepository;
    private final ExpenseService expenseService;

    public BudgetAllocation getBudgetAllocationByItineraryId(Long itineraryId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));

        // Check if userId is not equal to itinerary user id
        Long currentUserId = UserUtils.getCurrentUserId();
        if (!itinerary.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("User ID does not match the itinerary owner");
        }

        return itinerary.getBudgetAllocation();
    }

    public BudgetAllocation updateBudgetAllocation(Long itineraryId, BudgetAllocationDTO budgetAllocation) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));

        // Check if userId is not equal to itinerary user id
        Long currentUserId = UserUtils.getCurrentUserId();
        if (!itinerary.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("User ID does not match the itinerary owner");
        }
        BudgetAllocation existingBudgetAllocation = itinerary.getBudgetAllocation();
        if (budgetAllocation.getTotalBudget() < budgetAllocation.getAccommodation() +
                budgetAllocation.getTransportation() +
                budgetAllocation.getFood() +
                budgetAllocation.getActivities() +
                budgetAllocation.getEntertainment() +
                budgetAllocation.getShopping() +
                budgetAllocation.getOther()) {
            throw new IllegalArgumentException("Allocated budget exceeds the total budget");
        }
        existingBudgetAllocation.setAccommodation(budgetAllocation.getAccommodation());
        existingBudgetAllocation.setTransportation(budgetAllocation.getTransportation());
        existingBudgetAllocation.setFood(budgetAllocation.getFood());
        existingBudgetAllocation.setActivities(budgetAllocation.getActivities());
        existingBudgetAllocation.setEntertainment(budgetAllocation.getEntertainment());
        existingBudgetAllocation.setShopping(budgetAllocation.getShopping());
        existingBudgetAllocation.setOther(budgetAllocation.getOther());
        existingBudgetAllocation.setTotalBudget(budgetAllocation.getTotalBudget());
        BudgetAllocation savedBudgetAllocation = budgetAllocationRepository.save(existingBudgetAllocation);

        return savedBudgetAllocation;
    }

    public BudgetAllocationDTO mapToDTO(BudgetAllocation budgetAllocation) {
        return BudgetAllocationDTO.builder()
                .id(budgetAllocation.getId())
                .totalBudget(budgetAllocation.getTotalBudget())
                .accommodation(budgetAllocation.getAccommodation())
                .food(budgetAllocation.getFood())
                .activities(budgetAllocation.getActivities())
                .transportation(budgetAllocation.getTransportation())
                .shopping(budgetAllocation.getShopping())
                .entertainment(budgetAllocation.getEntertainment())
                .other(budgetAllocation.getOther())
                .accommodationSpent(budgetAllocation.calculateSpentByCategory(ExpenseCategory.ACCOMMODATION))
                .foodSpent(budgetAllocation.calculateSpentByCategory(ExpenseCategory.FOOD))
                .activitiesSpent(budgetAllocation.calculateSpentByCategory(ExpenseCategory.ACTIVITIES))
                .transportationSpent(budgetAllocation.calculateSpentByCategory(ExpenseCategory.TRANSPORTATION))
                .shoppingSpent(budgetAllocation.calculateSpentByCategory(ExpenseCategory.SHOPPING))
                .entertainmentSpent(budgetAllocation.calculateSpentByCategory(ExpenseCategory.ENTERTAINMENT))
                .otherSpent(budgetAllocation.calculateSpentByCategory(ExpenseCategory.OTHER))
                .expenses(budgetAllocation.getExpenses().stream()
                        .map(expenseService::mapToDTO)
                        .collect(Collectors.toList()))
                .remainingBudget(budgetAllocation.calculateRemainingBudget())
                .build();
    }
}
