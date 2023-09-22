package com.batchstudy.projecttwo.repository;

import com.batchstudy.projecttwo.dto.FlightOfferDto;
import com.batchstudy.projecttwo.model.FlightSearchResult;
import com.batchstudy.projecttwo.simulator.SimulatorFlightDto;
import com.batchstudy.projecttwo.simulator.SimulatorResponseDto;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public interface FlightSearchResultRepository extends CrudRepository<FlightSearchResult, Integer> {

    List<FlightSearchResult> findAllBySearchIdOrderByPrice(String searchId);

    default List<FlightOfferDto> findResults(String searchId) {
        return findAllBySearchIdOrderByPrice(searchId)
                .stream()
                .map(FlightOfferDto::new)
                .collect(Collectors.toList());
    }

    default void saveResultsToDatabase(SimulatorResponseDto responseDto, String searchId) {
        for (SimulatorFlightDto flightDto : responseDto.flights) {
            FlightSearchResult searchResult = new FlightSearchResult();
            searchResult.setPrice(flightDto.price);
            searchResult.setArrivalTime(flightDto.arrivalTime);
            searchResult.setDepartureTime(flightDto.departureTime);
            searchResult.setAirlineName(responseDto.airlineName);
            searchResult.setSearchId(searchId);
            save(searchResult);
        }

    }

    default void saveInternalOffer(BigDecimal price,
                                   LocalDateTime departureTime,
                                   LocalDateTime arrivalTime,
                                   String searchId) {
        FlightSearchResult searchResult = new FlightSearchResult();
        searchResult.setPrice(price);
        searchResult.setArrivalTime(arrivalTime);
        searchResult.setDepartureTime(departureTime);
        searchResult.setAirlineName("Holiday Check Airline");
        searchResult.setSearchId(searchId);
        save(searchResult);
    }
}