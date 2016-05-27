package com.nestof.paraweather.utils;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GeoLocation implements Serializable {

	private GeoLocationType type;

	private String latitude;

	private String longitude;
}
