package com.batchstudy.projecttwo.utils.converter;

import com.batchstudy.projecttwo.model.Airport;
import org.springframework.core.convert.converter.Converter;

public class StringToAirportConverter implements Converter<String, Airport> {

    @Override
    public Airport convert(String source) {
        return Airport.valueOf(source);
    }
}