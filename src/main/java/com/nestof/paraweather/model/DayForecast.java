package com.nestof.paraweather.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class DayForecast implements Serializable {

	private static final long serialVersionUID = -6245792299370788484L;

	private String lastUpdateTime;
	
	private String date;
	
	private Map<Integer, HourForecast> hourlyForecast = new HashMap<Integer, HourForecast>();
	
	private Map<Integer, HourForecast> threeHoursForecast =new HashMap<Integer, HourForecast>();
	
	private int sunnyHours;
	
	private String sunrise;
	
	private String sunset;
	
	private String pressure;
}
