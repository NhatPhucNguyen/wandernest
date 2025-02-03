package com.wn.wandernest.dtos;

import java.util.List;

import lombok.Data;

@Data
public class TripAdvisorActivityResponse {
    private List<ActivityData> data; // List of activities/attractions
    private PaginationInfo pagination; // Pagination details (if applicable)

    @Data
    public static class ActivityData {
        private String name; // Name of the activity/attraction
        private String address; // Address of the activity
        private String category; // Category (e.g., "MUSEUM", "BEACH", "HIKING")
        private double rating; // Average rating (e.g., 4.5)
        private double price; // Estimated cost (e.g., 20.0 for $20)
        private String description; // Short description of the activity
        private String website; // Official website URL
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
