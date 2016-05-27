package com.nestof.paraweather.service;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.nestof.paraweather.utils.GeoLocation;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.springframework.stereotype.Service;

/**
 * Created by csailly on 27/05/16.
 */
@Slf4j
@Service
public class GoogleMapsService {

    private GeoApiContext geoApiContext;

    public GoogleMapsService(){
        geoApiContext = new GeoApiContext().setApiKey("AIzaSyDyoEwVcYpU9DXDac8pEC4WJUZPsxXW0sg");
    }


    public DirectionsLeg getDirections(GeoLocation from, GeoLocation to){
        try {
            DirectionsResult directionsResult = DirectionsApi.getDirections(geoApiContext,from.getLatitude() + ","+from.getLongitude(), to.getLatitude() + ","+to.getLongitude()).departureTime(DateTime.now()).await();
            return directionsResult.routes[0].legs[0];
        } catch (Exception e) {
            log.error("Error try to get directions", e);
        }
        return null;
    }

}
