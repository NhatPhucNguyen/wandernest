package com.wn.wandernest.dtos;

import lombok.Data;

@Data
public class GoogleMapsPlace {
    private String name;
    private String vicinity;
    private int priceLevel;
}
