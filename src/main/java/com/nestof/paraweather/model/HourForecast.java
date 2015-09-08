package com.nestof.paraweather.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class HourForecast  implements Serializable  {

	private static final long serialVersionUID = 4261227162275423068L;

	private int hour;
	
	private String weatherLabel;
	
	private float temperature;
	
	private float windChill;
	
	private String windDirection;
	
	private float windSpeed;
	
	private float windGust;
	
	private String precips;
}
