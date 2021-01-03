package com.finder.atm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class ATMDetail {
    @JsonProperty("address")
    private Address address;
    @JsonProperty("distance")
    private Integer distance;
    @JsonProperty("type")
    private String type;
    @JsonProperty("functionality")
    private String[] functionality;
}
