package com.nestof.paraweather.service;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nestof.paraweather.model.DayForecast;
import com.nestof.paraweather.parser.MeteoBlueParser;
import com.nestof.paraweather.utils.GeoLocation;
import com.nestof.paraweather.utils.LocationConverter;

@Service
public class MeteoBlueService {

	private final static String URL = "https://www.meteoblue.com/fr/meteo/prevision/semaine/#{latitude}#{longitude}?day=#{day}";

	@Cacheable(value = "meteoBlueForecast")
	public DayForecast getForecast(GeoLocation geoLocation, LocalDate date) {
		long day = Period.between(LocalDate.now(), date).getDays() + 1;

		if (day < 1 || day > 7)
			return null;

		GeoLocation decimalGeoLocation = new LocationConverter()
				.toDecimalDegrees(geoLocation);

		MeteoBlueParser meteoBlueParser = new MeteoBlueParser();

		String callUrl = StringUtils.replace(URL, "#{latitude}",
				decimalGeoLocation.getLatitude());
		callUrl = StringUtils.replace(callUrl, "#{longitude}",
				decimalGeoLocation.getLongitude());
		callUrl = StringUtils.replace(callUrl, "#{day}", "" + day);

		meteoBlueParser.init(callUrl);
		return meteoBlueParser.getForecast();
	}
}
