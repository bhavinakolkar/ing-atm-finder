package com.finder.atm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class Address {
    @JsonProperty("street")
    private String street;
    @JsonProperty("housenumber")
    private String houseNumber;
    @JsonProperty("postalcode")
    private String postalCode;
    @JsonProperty("city")
    private String city;
    @JsonProperty("geoLocation")
    private GeoLocation geoLocation;
}
