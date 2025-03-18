package com.wn.wandernest.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wn.wandernest.dtos.ItineraryRequestDTO;
import com.wn.wandernest.dtos.ItineraryResponseDTO;
import com.wn.wandernest.enums.ItineraryStatus;
import com.wn.wandernest.models.BudgetAllocation;
import com.wn.wandernest.models.Itinerary;
import com.wn.wandernest.models.TravelPreferences;
import com.wn.wandernest.models.User;
import com.wn.wandernest.repositories.ItineraryRepository;
import com.wn.wandernest.repositories.UserRepository;
import com.wn.wandernest.utils.JwtTokenUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

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

        // 4. Create travel preferences from request
        TravelPreferences travelPreferences = new TravelPreferences();
        travelPreferences.setAccommodationType(itineraryRequest.getAccommodationType());
        travelPreferences.setCuisinePreferences(itineraryRequest.getCuisinePreferences());
        travelPreferences.setActivityInterests(itineraryRequest.getActivityInterests());
        // 5. Build and save itinerary
        Itinerary itinerary = Itinerary.builder()
                .destination(itineraryRequest.getDestination())
                .startDate(itineraryRequest.getStartDate())
                .endDate(itineraryRequest.getEndDate())
                .numberOfTravelers(itineraryRequest.getNumberOfTravelers())
                .totalBudget(itineraryRequest.getTotalBudget())
                .status(ItineraryStatus.DRAFT)
                .budgetAllocation(budget)
                .user(user.get())
                .travelPreferences(travelPreferences)
                .lat(itineraryRequest.getLocation().getLat())
                .lng(itineraryRequest.getLocation().getLng())
                .build();
        return itineraryRepository.save(itinerary);
    }

    public List<ItineraryResponseDTO> getItinerariesByUser(HttpServletRequest request) {
        final String header = request.getHeader("Authorization");
        final String token = header.replace("Bearer ", "");
        String username = jwtTokenUtil.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));
        List<Itinerary> itineraries = itineraryRepository.findByUser(user);

        List<ItineraryResponseDTO> response = itineraries.stream()
                .map(ItineraryResponseDTO::new)
                .toList();
        return response;
    }

    public void deleteItineraryById(Long id) {
        itineraryRepository.deleteById(id);
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

    public Itinerary updateItineraryStatus(Long id, ItineraryStatus status) {
        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));
        itinerary.setStatus(status);
        return itineraryRepository.save(itinerary);
    }

    private void validateRequest(ItineraryRequestDTO request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
}
