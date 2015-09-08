package com.nestof.paraweather.model;

import java.io.Serializable;

import com.nestof.paraweather.utils.GeoLocation;

public class SiteForecast implements Serializable {

	private String siteId;
	
	private GeoLocation geoLocation;
	
	private DayForecast dayForecast;
}
