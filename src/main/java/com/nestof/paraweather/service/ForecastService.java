package com.nestof.paraweather.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nestof.paraweather.model.DayForecast;
import com.nestof.paraweather.utils.GeoLocation;
import com.nestof.paraweather.utils.GeoLocationType;

@Service
public class ForecastService {

	@Autowired
	MeteoBlueService meteoBlueService;

	public DayForecast getForecast(LocalDate date, GeoLocation geoLocation) {
		return meteoBlueService.getForecast(geoLocation, date);
	}

}
