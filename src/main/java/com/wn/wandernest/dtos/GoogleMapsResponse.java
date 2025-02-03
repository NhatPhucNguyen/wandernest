package com.wn.wandernest.dtos;

import java.util.List;

import lombok.Data;

@Data
public class GoogleMapsResponse {
    private List<GoogleMapsPlace> results;
}
