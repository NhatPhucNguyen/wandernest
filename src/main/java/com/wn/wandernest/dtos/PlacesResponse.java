package com.wn.wandernest.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PlacesResponse {
    private List<PlaceData> results; // List of places
    private String status;

    @Data
    public static class PlaceData {
        public String business_status;
        public Geometry geometry;
        public String icon;
        public String icon_background_color;
        public String icon_mask_base_uri;
        public String name;
        public OpeningHours opening_hours;
        public ArrayList<Photo> photos;
        public String place_id;
        public int price_level;
        public double rating;
        public String reference;
        public String scope;
        public ArrayList<String> types;
        public int user_ratings_total;
        public String vicinity;

        public static class Geometry {
            public Location location;
        }

        public static class OpeningHours {
            public boolean open_now;
        }

        public static class Photo {
            public int height;
            public ArrayList<String> html_attributions;
            public String photo_reference;
            public int width;
        }
    }
}
