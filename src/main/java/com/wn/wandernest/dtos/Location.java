package com.wn.wandernest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    public double lat;
    public double lng;

    @Override
    public String toString() {
        return String.format("%s,%s", this.lat, this.lng);
    }
}
