package com.wn.wandernest.dtos;

import java.util.List;

import lombok.Data;

@Data
public class TripAdvisorRestaurantResponse {
    private List<RestaurantData> data; // List of restaurants
    private PaginationInfo pagination; // Pagination details (if applicable)

    @Data
    public static class RestaurantData {
        private String name; // Restaurant name
        private String address; // Restaurant address
        private String cuisine; // Type of cuisine (e.g., Italian, Mexican)
        private double rating; // Average rating (e.g., 4.5)
        private int priceLevel; // Price level (e.g., 1 for $, 2 for $$, etc.)
        private String website; // Restaurant website URL
        private String phone; // Contact number
        private Location location; // Latitude and longitude

        @Data
        public static class Location {
            private double latitude;
            private double longitude;
        }
    }

    @Data
    public static class PaginationInfo {
        private int totalResults; // Total number of results
        private int currentPage; // Current page number
        private int pageSize; // Number of results per page
    }
}
