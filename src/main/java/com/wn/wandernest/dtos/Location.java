package com.wn.wandernest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Location {
    public double lat;
    public double lng;

    @Override
    public String toString() {
        return String.format("%s,%s", this.lat, this.lng);
    }
}
