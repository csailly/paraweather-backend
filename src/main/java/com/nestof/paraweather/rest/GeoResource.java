package com.nestof.paraweather.rest;

import com.google.maps.model.DirectionsLeg;
import com.nestof.paraweather.service.GoogleMapsService;
import com.nestof.paraweather.utils.GeoLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/geo")
public class GeoResource {

    private static Logger log = LoggerFactory.getLogger(GeoResource.class);

    @Autowired
    private GoogleMapsService googleMapsService;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/directions/from/{fromLatitude}/{fromLongitude}/to/{toLatitude}/{toLongitude}", method = RequestMethod.GET)
    public DirectionsLeg getDirections(@PathVariable String fromLatitude,
                                       @PathVariable String fromLongitude,
                                       @PathVariable String toLatitude,
                                       @PathVariable String toLongitude,
                                       HttpServletResponse response) {

        log.debug("REQUEST to get directions from {},{} to {},{}", fromLatitude, fromLongitude, toLatitude, toLongitude);

        try {
            GeoLocation from = GeoLocation.builder().latitude(fromLatitude).longitude(fromLongitude).build();
            GeoLocation to = GeoLocation.builder().latitude(toLatitude).longitude(toLongitude).build();

            return googleMapsService.getDirections(from, to);
        } catch (Exception e) {
            log.error("Error occured when get forecast :", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }
}
