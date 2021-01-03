package com.finder.atm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class GeoLocation {
    @JsonProperty("lat")
    private String lat;
    @JsonProperty("lng")
    private String lng;
}
