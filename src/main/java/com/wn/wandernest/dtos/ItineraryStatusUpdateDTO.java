package com.wn.wandernest.dtos;

import com.wn.wandernest.enums.ItineraryStatus;

import lombok.Data;

@Data
public class ItineraryStatusUpdateDTO {
    private ItineraryStatus status;
}