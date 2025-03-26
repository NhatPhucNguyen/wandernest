package com.wn.wandernest.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
import com.wn.wandernest.utils.UserUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public Itinerary generateItinerary(ItineraryRequestDTO itineraryRequest) {
        // 1. Validate input
        validateRequest(itineraryRequest);

        // 2. Allocate budget
        BudgetAllocation budget = allocateBudget(itineraryRequest.getTotalBudget(),
                itineraryRequest.getNumberOfTravelers(),
                itineraryRequest.getStartDate(), itineraryRequest.getEndDate());
        // 3.Get user
        User user = UserUtils.getCurrentUser();

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
                .user(user)
                .travelPreferences(travelPreferences)
                .lat(itineraryRequest.getLocation().getLat())
                .lng(itineraryRequest.getLocation().getLng())
                .build();
        budget.setItinerary(itinerary);
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
        BudgetAllocation budget = new BudgetAllocation();
        budget.setTotalBudget(totalBudget);
        // Calculate trip duration
        long tripDuration = ChronoUnit.DAYS.between(startDate, endDate);

        // Calculate daily budget per person
        double dailyBudgetPerPerson = totalBudget / (tripDuration * numberOfTravelers);

        // Update budget allocation fields
        budget.setAccommodation(dailyBudgetPerPerson * 0.4 * tripDuration * numberOfTravelers);
        budget.setFood(dailyBudgetPerPerson * 0.3 * tripDuration * numberOfTravelers);
        budget.setActivities(dailyBudgetPerPerson * 0.2 * tripDuration * numberOfTravelers);
        budget.setTransportation(dailyBudgetPerPerson * 0.1 * tripDuration * numberOfTravelers);

        // Optionally, set other fields like shopping, entertainment, etc., if
        // applicable
        budget.setShopping(0); // Example: Set to 0 or calculate dynamically
        budget.setEntertainment(0); // Example: Set to 0 or calculate dynamically
        budget.setOther(0); // Example: Set to 0 or calculate dynamically

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
