package com.wn.wandernest.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wn.wandernest.dtos.ExpenseDTO;
import com.wn.wandernest.models.Expense;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.repositories.ExpenseRepository;
import com.wn.wandernest.repositories.ItineraryRepository;
import com.wn.wandernest.utils.UserUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ItineraryRepository itineraryRepository;

    /**
     * Get all expenses for a specific itinerary.
     *
     * @param itineraryId The ID of the itinerary.
     * @return List of expenses.
     */
    public List<Expense> getExpensesByItinerary(Long itineraryId) {
        Itinerary itinerary = validateUserAccessToItinerary(itineraryId);
        return itinerary.getBudgetAllocation().getExpenses();
    }

    /**
     * Get a specific expense by its ID.
     *
     * @param expenseId The ID of the expense.
     * @return The Expense object.
     */
    public Expense getExpenseById(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found"));
        validateUserAccessToItinerary(expense.getBudgetAllocation().getItinerary().getId());
        return expense;
    }

    /**
     * Create a new expense for a specific itinerary.
     *
     * @param itineraryId The ID of the itinerary.
     * @param expense     The Expense object to create.
     * @return The created Expense object.
     */
    public Expense createExpense(Long itineraryId, Expense expense) {
        Itinerary itinerary = validateUserAccessToItinerary(itineraryId);
        expense.setBudgetAllocation(itinerary.getBudgetAllocation());
        return expenseRepository.save(expense);
    }

    /**
     * Update an existing expense.
     *
     * @param expenseId      The ID of the expense to update.
     * @param updatedExpense The updated Expense object.
     * @return The updated Expense object.
     */
    public Expense updateExpense(Long expenseId, Expense updatedExpense) {
        Expense existingExpense = getExpenseById(expenseId);
        existingExpense.setDescription(updatedExpense.getDescription());
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setCategory(updatedExpense.getCategory());
        existingExpense.setDate(updatedExpense.getDate());
        return expenseRepository.save(existingExpense);
    }

    /**
     * Delete an expense by its ID.
     *
     * @param expenseId The ID of the expense to delete.
     */
    public void deleteExpense(Long expenseId) {
        Expense expense = getExpenseById(expenseId);
        expenseRepository.delete(expense);
    }

    /**
     * Validate that the current user has access to the itinerary.
     *
     * @param itineraryId The ID of the itinerary.
     * @return The Itinerary object if the user has access.
     */
    private Itinerary validateUserAccessToItinerary(Long itineraryId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));
        Long currentUserId = UserUtils.getCurrentUserId();
        if (!itinerary.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("You do not have access to this itinerary");
        }
        return itinerary;
    }

    /**
     * Map an Expense entity to an ExpenseDTO.
     *
     * @param expense The Expense entity.
     * @return The ExpenseDTO.
     */
    public ExpenseDTO mapToDTO(Expense expense) {
        return ExpenseDTO.builder()
                .id(expense.getId())
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .date(expense.getDate())
                .itineraryId(expense.getBudgetAllocation().getItinerary().getId())
                .itineraryDestination(expense.getBudgetAllocation().getItinerary().getDestination())
                .build();
    }

    /**
     * Get all expenses for a specific itinerary as DTOs.
     *
     * @param itineraryId The ID of the itinerary.
     * @return List of ExpenseDTOs.
     */
    public List<ExpenseDTO> getExpensesByItineraryAsDTO(Long itineraryId) {
        List<Expense> expenses = getExpensesByItinerary(itineraryId);
        return expenses.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Get a specific expense as a DTO.
     *
     * @param expenseId The ID of the expense.
     * @return The ExpenseDTO.
     */
    public ExpenseDTO getExpenseByIdAsDTO(Long expenseId) {
        Expense expense = getExpenseById(expenseId);
        return mapToDTO(expense);
    }
}