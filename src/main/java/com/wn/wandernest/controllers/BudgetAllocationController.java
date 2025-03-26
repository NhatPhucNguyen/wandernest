package com.wn.wandernest.controllers;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wn.wandernest.dtos.BudgetAllocationDTO;
import com.wn.wandernest.models.BudgetAllocation;
import com.wn.wandernest.services.BudgetAllocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budgetAllocations")
public class BudgetAllocationController {
    private final BudgetAllocationService budgetAllocationService;

    @RequestMapping
    public BudgetAllocationDTO getBudgetAllocation(@RequestParam Long itineraryId) {
        BudgetAllocation budgetAllocation = budgetAllocationService.getBudgetAllocationByItineraryId(itineraryId);
        return budgetAllocationService.mapToDTO(budgetAllocation);
    }

    @PutMapping
    public BudgetAllocationDTO updateBudgetAllocation(@RequestParam Long itineraryId,
            @RequestBody BudgetAllocationDTO budgetAllocation) {
        BudgetAllocation savedBudgetAllocation = budgetAllocationService.updateBudgetAllocation(itineraryId,
                budgetAllocation);
        return budgetAllocationService.mapToDTO(savedBudgetAllocation);
    }
}
