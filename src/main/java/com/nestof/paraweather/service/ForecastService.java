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

	public DayForecast getForecast(LocalDate date) {
		//1° Récupérer les sites
		
		//2° Pour chaque site récupérer les prévisions
		
		//3° Renvoyer une liste de prévisions
		
		
		
		GeoLocation geoLocation = new GeoLocation(GeoLocationType.DD,"50.43N", "2.49E");
		
		return meteoBlueService.getForecast(geoLocation, date);
	}

}
