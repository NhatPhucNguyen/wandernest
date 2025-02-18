package com.wn.wandernest.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;

@Getter
public class PlacesResponse {
    private List<PlaceData> places; // List of places

    @Data
    public static class PlaceData {
        public String id;
        public ArrayList<String> types;
        public String formattedAddress;
        public PlaceLocation location;
        public double rating;
        public String websiteUri;
        public String priceLevel;
        public DisplayName displayName;
        public CurrentOpeningHours currentOpeningHours;
        public ArrayList<Photo> photos;
        public PriceRange priceRange;

        public static class Geometry {
            public Location location;
        }

        public static class OpeningHours {
            public boolean open_now;
        }

        public static class Photo {
            public String name;
            public int widthPx;
            public int heightPx;
        }

        public static class DisplayName {
            public String text;
            public String languageCode;
        }

        public static class PriceRange {
            public PriceData startPrice;
            public PriceData endPrice;
        }

        public static class PriceData {
            public String currencyCode;
            public String units;
        }

        public static class CurrentOpeningHours {
            public boolean openNow;
            public ArrayList<String> weekdayDescriptions;
        }

        public static class PlaceLocation {
            public double latitude;
            public double longitude;
        }
    }
}
