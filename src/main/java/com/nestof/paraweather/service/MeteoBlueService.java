package com.nestof.paraweather.service;

import com.nestof.paraweather.model.DayForecast;
import com.nestof.paraweather.parser.MeteoBlueParser;
import com.nestof.paraweather.utils.GeoLocation;
import com.nestof.paraweather.utils.LocationConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class MeteoBlueService {

    private final static String URL = "https://www.meteoblue.com/fr/meteo/prevision/semaine/#{latitude}N#{longitude}E?day=#{day}";

    public DayForecast getForecast(GeoLocation geoLocation, LocalDate date) {
        long day = Period.between(LocalDate.now(), date).getDays() + 1;

        if (day < 1 || day > 7)
            return null;

        GeoLocation decimalGeoLocation = new LocationConverter()
            .toDecimalDegrees(geoLocation);

        MeteoBlueParser meteoBlueParser = new MeteoBlueParser();

        String callUrl = generateCallUrl(decimalGeoLocation.getLatitude(), decimalGeoLocation.getLongitude(), day);
        meteoBlueParser.init(callUrl);

        DayForecast forecast = meteoBlueParser.getForecast();

        //Check forecast date
        LocalDate forecastDate = LocalDate.parse(forecast.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        if(Period.between(forecastDate, date).getDays() != 0){
            log.info("Dates differ asked {}, retrieved {}", date, forecastDate);
            //Requested date differs to retrieved date, this appears when current day forecast is not enable anymore on MeteoBlue Website
            day--;//day = 1 is now tomorrow
            callUrl = generateCallUrl(decimalGeoLocation.getLatitude(), decimalGeoLocation.getLongitude(), day);
            meteoBlueParser.init(callUrl);
            forecast = meteoBlueParser.getForecast();
        }

        return forecast;
    }


    private String generateCallUrl(String latitude, String longitude, long day){
        String callUrl = StringUtils.replace(URL, "#{latitude}",
            latitude);
        callUrl = StringUtils.replace(callUrl, "#{longitude}",
            longitude);
        callUrl = StringUtils.replace(callUrl, "#{day}", "" + day);

        return callUrl;
    }
}
