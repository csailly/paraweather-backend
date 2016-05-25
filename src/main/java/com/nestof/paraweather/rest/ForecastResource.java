package com.nestof.paraweather.rest;

import com.nestof.paraweather.model.DayForecast;
import com.nestof.paraweather.service.ForecastService;
import com.nestof.paraweather.utils.GeoLocation;
import com.nestof.paraweather.utils.GeoLocationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/forecast")
public class ForecastResource {

    private static Logger log = LoggerFactory.getLogger(ForecastResource.class);

    @Autowired
    private ForecastService forecastService;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/byLocation/lat/{latitude}/lng/{longitude}/date/{date}", method = RequestMethod.GET)
    public DayForecast getForecast(@PathVariable String date,
                                  @PathVariable String latitude,
                                  @PathVariable String longitude,
                                  HttpServletResponse response) {
        log.debug("Rest request to get forecast for date {} at lat {} - lng {}", date, latitude, longitude);


        try {
            LocalDate localDate = LocalDate.parse(
                date, DateTimeFormatter.ofPattern("yyyyMMdd"));

            GeoLocation geoLocation = new GeoLocation(GeoLocationType.DD,latitude, longitude);

            return forecastService.getForecast(localDate, geoLocation);
        } catch (Exception e) {
            log.error("Error occured when get forecast :", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }
}
