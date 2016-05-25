package com.nestof.paraweather.utils;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

/**
 * LatLongConverter Class - converts Latitude/Longitude values between DMS and
 * Decimal Degrees Copyright (C) 2010 David Cox
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see http://www.gnu.org/licenses/
 *
 */
@Data
public class LocationConverter {
	private String type;
	private String degrees;
	private String minutes;
	private String seconds;
	private String direction;

	/**
	 * Returns a formatted DMS String. ie. DD MM' SS" E Uses values loaded into
	 * Degrees, Minutes and Seconds
	 *
	 * @return String
	 */
	private String toFormattedDMSString() {

		StringBuffer sb = new StringBuffer();
		String dq = "\"";
		String sq = "'";

		sb.append(getDegrees() + (char) (176) + " " + getMinutes() + sq + " "
				+ getSeconds() + dq + " " + getDirection());

		return sb.toString();
	}

	/**
	 * Returns the Decimal Degrees that corresponds to the DMS values entered.
	 * Requires values in Degrees, Minutes, Seconds, and Direction
	 *
	 * @return String
	 */
	public String toDecimalDegrees(String location) {
		degrees = StringUtils.substringBefore(location, "°").trim();
		minutes = StringUtils.substringBetween(location, "°", "'").trim();
		seconds = StringUtils.substringBetween(location, "'", "\"").trim();
		direction = StringUtils.substringAfter(location, "\"").trim();

		String returnString = null;

		Double dblDegree;
		Double dblMinutes;
		Double dblSeconds;
		Double decDegrees;
		String CompassDirection;

		dblDegree = Double.parseDouble(getDegrees());
		dblMinutes = Double.parseDouble(getMinutes());
		dblSeconds = Double.parseDouble(getSeconds());

		CompassDirection = getDirection();

		decDegrees = dblDegree + (dblMinutes / 60) + (dblSeconds / 3600);

		if (CompassDirection.equalsIgnoreCase("S"))
			decDegrees = decDegrees * -1;

		if (CompassDirection.equalsIgnoreCase("W"))
			decDegrees = decDegrees * -1;

		returnString = decDegrees.toString()+direction;

		return returnString;
	}

	public GeoLocation toDecimalDegrees(GeoLocation geoLocation){
		if (GeoLocationType.DD.equals(geoLocation.getType())){
			return geoLocation;
		}

		return new GeoLocation(GeoLocationType.DD, toDecimalDegrees(geoLocation.getLatitude()), toDecimalDegrees(geoLocation.getLongitude()));
	}


	/**
	 * Returns a formatted DMS String using value loaded into DecimalDegrees ie.
	 * DD MM' SS" E
	 *
	 * @return String
	 */
	public String toDMS(String DecimalDegreesAndMinutes, String type) {
		this.type = type;

		String returnstring = null;
		Double DegreesAndMinutes;
		Integer decDegrees;
		Integer decMinutes;
		Double decSeconds;

		DegreesAndMinutes = Double.parseDouble(DecimalDegreesAndMinutes);
		decDegrees = (int) DegreesAndMinutes.doubleValue();
		decMinutes = (int) ((DegreesAndMinutes.doubleValue() - decDegrees) * 60);
		decSeconds = ((((DegreesAndMinutes.doubleValue() - decDegrees) * 60) - decMinutes) * 60);

		decDegrees = Math.abs(decDegrees);
		decMinutes = Math.abs(decMinutes);
		decSeconds = Math.abs(decSeconds);

		setDegrees(decDegrees.toString());
		setMinutes(decMinutes.toString());
		setSeconds(decSeconds.toString());

		if (getType().equalsIgnoreCase("Latitude")) {
			if (Double.parseDouble(getDegrees()) < 0) {
				setDirection("S");
			} else {
				setDirection("N");
			}
		}

		if (getType().equalsIgnoreCase("Longitude")) {
			if (Double.parseDouble(getDegrees()) < 0) {
				setDirection("W");
			} else {
				setDirection("E");
			}
		}

		returnstring = toFormattedDMSString();

		return returnstring;

	}

	public GeoLocation toDMS(GeoLocation geoLocation){
		if (GeoLocationType.DMS.equals(geoLocation.getType())){
			return geoLocation;
		}
		return new GeoLocation(GeoLocationType.DMS,toDMS(geoLocation.getLatitude(),"Latitude"), toDMS(geoLocation.getLongitude(),"Longitude"));
	}


/*	public static void main(String[] args) {
		String latitude = "50° 25' 00.0\" N";
		String longitude = "2° 30' 46.0\" E";

		GeoLocation geoLocation = new GeoLocation(GeoLocationType.DMS,latitude, longitude);



		LocationConverter locationConverter = new LocationConverter();
		System.out.println(locationConverter.toDecimalDegrees(geoLocation));

		latitude = "50.41";
		longitude = "2.51";
		geoLocation = new GeoLocation(GeoLocationType.DD, latitude, longitude);

		System.out.println(locationConverter.toDMS(geoLocation));
	}*/

}
