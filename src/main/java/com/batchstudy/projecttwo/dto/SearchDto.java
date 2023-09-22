package com.batchstudy.projecttwo.dto;


import com.batchstudy.projecttwo.model.Airport;

import java.time.LocalDate;

public class SearchDto {

    public LocalDate flightDate;
    public Airport departureAirport;
    public Airport arrivalAirport;
}