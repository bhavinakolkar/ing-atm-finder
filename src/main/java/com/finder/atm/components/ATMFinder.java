package com.finder.atm.components;

import com.finder.atm.model.ATMDetail;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ATMFinder {

    private Map<String, List<ATMDetail>> atmDetailMap;
    private List<ATMDetail> atmDetailList;

    /**
     * This method is responsible to filter `ING` ATM and convert it into {@link Map}
     *
     * @param output
     */
    public void processATMDetails(List<ATMDetail> output) {
        // Filter only `ING` Type, Don't include `GELDMAAT`
        atmDetailList = output.stream().filter(atmDetail -> atmDetail.getType().toLowerCase().equals("ing")).collect(Collectors.toList());
    }

    /**
     * Fetch all {@link ATMDetail}
     *
     * @return
     */
    public List<ATMDetail> findAll() {
        return Collections.unmodifiableList(atmDetailList);
    }

    /**
     * Fetch all list of cities
     *
     * @return
     */
    public Set<String> findAllCities() {
        atmDetailMap = atmDetailList.stream().collect(Collectors.groupingBy(atmDetail -> atmDetail.getAddress().getCity()));
        return Collections.unmodifiableSet(atmDetailMap.keySet());
    }

    /**
     * Fetch {@link ATMDetail} by city
     *
     * @param city
     * @return
     */
    public List<ATMDetail> findByCity(final String city) {
        atmDetailMap = atmDetailList.stream().collect(Collectors.groupingBy(atmDetail -> atmDetail.getAddress().getCity()));
        return atmDetailMap.containsKey(city) ? Collections.unmodifiableList(atmDetailMap.get(city)) : Collections.emptyList();
    }
}
