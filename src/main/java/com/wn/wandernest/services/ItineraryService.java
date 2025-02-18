package com.wn.wandernest.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wn.wandernest.dtos.ItineraryRequestDTO;
import com.wn.wandernest.enums.ItineraryStatus;
import com.wn.wandernest.models.BudgetAllocation;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.models.User;
import com.wn.wandernest.repositories.ItineraryRepository;
import com.wn.wandernest.repositories.UserRepository;
import com.wn.wandernest.utils.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    // TODO: Modify generateItinerary
    public Itinerary generateItinerary(HttpServletRequest request, ItineraryRequestDTO itineraryRequest) {
        // 1. Validate input
        validateRequest(itineraryRequest);

        // 2. Allocate budget
        BudgetAllocation budget = allocateBudget(itineraryRequest.getTotalBudget(),
                itineraryRequest.getNumberOfTravelers(),
                itineraryRequest.getStartDate(), itineraryRequest.getEndDate());
        // 3.Get user id
        final String header = request.getHeader("Authorization");
        final String token = header.replace("Bearer ", "");
        String username = jwtTokenUtil.extractUsername(token);
        Optional<User> user = userRepository.findByUsername(username);
        // 6. Build and save itinerary
        Itinerary itinerary = Itinerary.builder()
                .destination(itineraryRequest.getDestination())
                .startDate(itineraryRequest.getStartDate())
                .endDate(itineraryRequest.getEndDate())
                .numberOfTravelers(itineraryRequest.getNumberOfTravelers())
                .totalBudget(itineraryRequest.getTotalBudget())
                .status(ItineraryStatus.DRAFT)
                .budgetAllocation(budget)
                .user(user.get())
                .build();
        return itineraryRepository.save(itinerary);
    }

    public List<Itinerary> getItinerariesByUser(HttpServletRequest request) {
        final String header = request.getHeader("Authorization");
        final String token = header.replace("Bearer ", "");
        String username = jwtTokenUtil.extractUsername(token);
        Optional<User> user = userRepository.findByUsername(username);
        return itineraryRepository.findByUser(user.get());
    }

    private BudgetAllocation allocateBudget(double totalBudget, int numberOfTravelers, LocalDate startDate,
            LocalDate endDate) {
        // Example: Simple budget allocation logic

        long tripDuration = ChronoUnit.DAYS.between(startDate, endDate);

        // Example: Dynamic budget allocation logic
        double dailyBudgetPerPerson = totalBudget / (tripDuration * numberOfTravelers);

        BudgetAllocation budget = new BudgetAllocation();
        budget.setAccommodation(dailyBudgetPerPerson * 0.4 * tripDuration * numberOfTravelers);
        budget.setMeals(dailyBudgetPerPerson * 0.3 * tripDuration * numberOfTravelers);
        budget.setActivities(dailyBudgetPerPerson * 0.2 * tripDuration * numberOfTravelers);
        budget.setTransportation(dailyBudgetPerPerson * 0.1 * tripDuration * numberOfTravelers);
        return budget;
    }

    private void validateRequest(ItineraryRequestDTO request) {
        // Validate dates, budget, etc.
    }
}
