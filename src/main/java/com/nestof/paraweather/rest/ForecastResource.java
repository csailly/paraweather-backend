package com.nestof.paraweather.rest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nestof.paraweather.model.DayForecast;
import com.nestof.paraweather.service.ForecastService;

@RestController
@RequestMapping("/forecast")
public class ForecastResource {
	
	private static Logger log = LoggerFactory.getLogger(ForecastResource.class);

	@Autowired
	private ForecastService forecastService;

	@RequestMapping(value = "/{date}", method = RequestMethod.GET)
	public DayForecast getForcast(@PathVariable String date, HttpServletResponse response) {
		log.debug("Rest request to get forecast for date {}", date);
		
		
		try {
			LocalDate localDate = LocalDate.parse(
					date, DateTimeFormatter.ofPattern("yyyyMMdd"));
			
			return forecastService.getForecast(localDate);
		} catch (Exception e) {
			log.error("Error occured when get forecast :",e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}
}
